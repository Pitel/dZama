#!/usr/bin/perl -w
##############################################################################
# program stats.pl
# napovedu k pouziti scriptu zobrazite ./stats.pl --help


use strict;
use warnings;
use Data::Dumper;
use Cwd;


# konstanty

#chyba u parametru
our $PARAM_ERROR = 
	"Chyba v parametrech! Napovedu zobrazite ./stats.pl --help\n";

# klicova slova pro Javu
our @JAVA_KEYWORDS = 
qw(
abstract assert boolean break byte case catch char class const continue 
default do double else enum extends final finally float for goto if implements
import instanceof int interface long native new package private protected 
public return short static strictfp super switch synchronized this throw 
throws transient try void volatile while
);

# operatory Javy vhodne serazene (nalezene se mazaji z pameti)
our @JAVA_OPERATORS = 
qw(
\/   %   \+\+  --  ==  \!=  <<= >>= >>>= &=     
>=   <=   &&    \|\|    \? instanceof ~  >>>  <<   >>   
\+= -= \*= \/= 
\^= \|= = \* \+ - \! > < \| & \^
);


#lokalni promenne

my $operation;                #operace, ktera se ma provest 
my $full_path;            #vypysovat 1 nebo nevypisovat 0 cestu u souboru
my @files;                    #seznam se soubory
my @files_name;
my @counts;
my $file_name;
my $count = 0;
my $pattern;


# kontrola a zpracovani vstupnich parametru

($operation, $full_path) = &check_parameters;


# projdeme soubory v adresarove strukture a dame je do seznamu
@files = list_of_file("./");

#rozhodovani co provedeme
if ($operation eq "k") {													#budeme hledat klicova slova
			
	#iterace vsemi soubory
	foreach $file_name (@files) {
		$count = search_k($file_name);
		@files_name = (@files_name, $file_name);
		@counts = (@counts, $count);    
	}	

} elsif ($operation eq "c") {											#budeme pocitat byty komentaru
	
	#iterace vsemi soubory
	foreach $file_name (@files) {
		$count = count_notes($file_name);
		
		#zapiseme do pole vysledku
		@files_name = (@files_name, $file_name);
		@counts = (@counts, $count);
	}

} elsif ($operation eq "o") {											#budeme hledat operatory
		
	#iterace vsemi soubory
	foreach $file_name (@files) {
		$count = search_o($file_name);
		
		#zapiseme do pole vysledku
		@files_name = (@files_name, $file_name);
		@counts = (@counts, $count);    
	}		
		
} elsif ($operation eq "i") {											#budeme hledat identifikatory
																									# bez klicovych slov  
	#iterace vsemi soubory
	foreach $file_name (@files) {
		$count = search_i($file_name);
		
		#zapiseme do pole vysledku
		@files_name = (@files_name, $file_name);
		@counts = (@counts, $count);    
	}		
	
} elsif ($operation eq "ik") {										#budeme hledat iden. vc. klic.
	
	#iterace vsemi soubory
	foreach $file_name (@files) {
		$count = search_ik($file_name);
		
		#zapiseme do pole vysledku
		@files_name = (@files_name, $file_name);
		@counts = (@counts, $count);    
	}		

} elsif ($operation =~ /(w) (.*)/) {							#budeme hledat pattern
	#hledany vzor
	$pattern = $2;
	
	#iterace vsemi soubory
	foreach $file_name (@files) {
		$count = search_pattern($file_name,$pattern);
		
		#zapiseme do pole vysledku
		@files_name = (@files_name, $file_name);
		@counts = (@counts, $count);      
	}	
} elsif ($operation eq "-help") {

	&print_help();

} else {																				#neplatny parametr
	print $PARAM_ERROR;
	exit 1;		
}

#tisk vysledku
print_result(\@files_name,\@counts,$full_path);

exit 0;


###########################################################################
# Podprogramy
###########################################################################



###########################################################################
# Zkontroluje vstupni parametry
# parametry: paramtry prikazove radky
# vraci pozadovanou operaci a zda vypisovat celou cestu
sub check_parameters {
	
	my $parametrs = join(" ", @ARGV);
	my @temp_param = ();
	my @params = ();
	my $full_path = 1;


	#rozdeleni dle ' -'
	@temp_param = split(" -", $parametrs);
	
	# pokud nebyl zadan zadny parametr, tak chyba
	if ($#temp_param == -1) {
 		print $PARAM_ERROR;
		exit 1;
	}
	
	#odstraneni prvniho '-'
	$temp_param[0] = substr($temp_param[0], 1, length($temp_param[0]));
	
	#test na p
	@params = grep(/[^p]/, @temp_param);
	if ($#temp_param != $#params) {
		$full_path = 0;	
	} 
	
	#jestli nezbyva posledni parametr, je neco spatne
	if ($#params != 0) {
 		print $PARAM_ERROR;
		exit 1;		
	}	
	
	return $params[0], $full_path;

} # end sub check_parameters


###########################################################################
#nacte z daneho adresare vsechny .java soubory
# pouzito rekurzivne!
#parametry: absolutni cesta k adreari
# vraci seznam souboru s abs. cestou  
sub list_of_file {  
	my($dir) = @_;
	my $directory;
	my  $file;
	
	opendir($directory, $dir) or die "Nepodařilo se otevřít $dir: $!";

	#projdeme soubory a adresare
	while (my  $file = readdir $directory) {
		next if  $file eq "." or  $file eq "..";
		if (-f $dir."/". $file) {
      if($file =~ /.*\.java$/) {							#test na .java soubor
     		#korekce cesty
        if ($dir eq "./") {
     			@files = (@files, $dir.$file);
     		} else {
     			@files = (@files, $dir."/".$file);
     		}	
      }
		} elsif(-d $dir."/". $file) {            #jedna se o adresar? 
		  #korekce cesty	
      if ($dir eq "./") {
				list_of_file($dir.$file);
			} else {
				list_of_file($dir."/".$file);
			}	
		}
	}
  closedir $directory;
  return @files;
}

###########################################################################
#najde identifiaktory. Ignoruje keywords, komentare a retezce.
# paramter cesta k souboru
# vrati pocet identifikatoru
sub search_k {

	my($file_name) = @_;
	my $java_file;
	my @result;
	my $counter = 0;	
	my $word;
	
	#prevedeme soubor na string
	$java_file = &file_to_string($file_name);
	
	# odstranime komentare a retezce
	$java_file = &remove_comment_string($java_file);	
	
	#hledame klicova slova
	foreach $word (@JAVA_KEYWORDS) {
		@result = $java_file =~ /\b($word)\b/g;
		$counter += $#result+1;

	} 
	
	return $counter;
}

###########################################################################
#najde identifiaktory. Ignoruje keywords, komentare a retezce.
# paramter cesta k souboru
# vrati pocet identifikatoru
sub search_i {

	my($file_name) = @_;
	my $java_file;
	my @result;
	my $counter = 0;	
	my $word;
	
	#prevedeme soubor na string
	$java_file = &file_to_string($file_name);
	
	# odstranime komentare a retezce
	$java_file = &remove_comment_string($java_file);
	
	#odstranit true, false, null
	$java_file =~ s/(true|false|null)/ /g;

		
	#odstranime klicova slova  
	foreach $word (@JAVA_KEYWORDS) {
		$java_file =~ s/$word/ /g;
	}

  #odstranime vsechny cisla jako .123e+123 atd.
	$java_file = &remove_numbers($java_file);
	

	@result = $java_file =~ /([a-zA-Z_]{1}(\w|\$)*)/g;
	$counter = ($#result+1)/2; #/2 kvuli dvoum zavorkam v regexpu  	
	
	return $counter;
}

###########################################################################
#najde identifiaktory. Ignoruje keywords, komentare a retezce.
# paramter cesta k souboru
# vrati pocet identifikatoru
sub search_ik {

	my($file_name) = @_;
	my $java_file;
	my @result;
	my $counter = 0;	

	
	#prevedeme soubor na string
	$java_file = &file_to_string($file_name);
	
	# odstranime komentare a retezce
	$java_file = &remove_comment_string($java_file);
	
	#odstranit true, false, null
	$java_file =~ s/(true|false|null)/ /g;
		
	#odstranime vsechny cisla jako .123e+123 atd.
	$java_file = &remove_numbers($java_file);
		
	
	#spocitame identifikatory i klicova slova
	@result = $java_file =~ /([a-zA-Z_]{1}(\w|\$)*)/g;
	
	$counter += ($#result+1)/2; #/2 kvuli dvoum zavorkam v regexpu  	

	return $counter;
}

###########################################################################
# Najdeme operatory. Ignoruje komentare a retezce.
# paramter cesta k souboru
# vrati pocet identifikatoru
sub search_o {

	my($file_name) = @_;
	my $java_file;
	my @result;
	my $counter = 0;	
	my $word;
	
	#prevedeme soubor na string
	$java_file = &file_to_string($file_name);
	
	# odstranime komentare a retezce
	$java_file = &remove_comment_string($java_file);	
	
	#odstranime klicova slova  
	foreach $word (@JAVA_KEYWORDS) {
		$java_file =~ s/$word/ /g;
	}
			
	#odstranime radky s import * 
	$java_file =~ s/^import.*/ /g;  

	#odstranime vsechny cisla jako .123e+123 atd.
	$java_file = &remove_numbers($java_file);
	
	#najdeme operatory
	foreach $word (@JAVA_OPERATORS) {
		@result = $java_file =~ /$word/g;
		$counter += $#result+1;
		
#				# DEVELOP
#       while ($java_file =~ /($word)/g) {
#         print $1."\n";
#       }
		
		#smazeme jiz zaindexovane slovo
		$java_file =~ s/$word.*/ /g;   

	}	
	
	return $counter;
}


###########################################################################
#najde vzor
# paramter cesta k souboru, vzor
# vrati cislo
sub search_pattern {
	my($file_name,$pattern) = @_;
	my $java_file;
	my @result;
	
	#prevedeme soubor na string
	$java_file = &file_to_string($file_name);
	
	#upravime pattern aby to nebyl regular
	$pattern =~ s/(\\|\||\(|\)|\^|\$|\[|\]|\.|\*|\+|\?|\{){1}/\\$1/g;

	#provedeme hledani
	@result = $java_file =~ /($pattern)/g;
	
	
	return $#result+1;			#+1 kvuli poli

} #end sub search_pattern


###########################################################################
#vrati delku poznamek v bytech
# paramter cesta k souboru
# vrati cislo
sub count_notes {

	my($file_name) = @_;
  my $java_file;
  my $position1;
  my $position2;
  my $byte_counter = 0;
    
	#prevedeme soubor na string
	$java_file = &file_to_string($file_name);
	
  
  #komentare typu /* */
  while ($java_file =~ /(\/\*)/g) {
    $position1 = pos($java_file)-2;
    $java_file =~ /(\*\/)/g;
    $position2 = pos($java_file);
    $byte_counter += $position2 - $position1;
  	
  	#odstranime uz zpracovane komentare typu /* */
  	substr($java_file,$position1,$position2 - $position1," ");
  }
  
  
  while ($java_file =~ /(\/\/)/g ) {
    # bereme i s //
    $position1 = pos($java_file)-2;
    
    #oblast do konce radku
    $java_file =~ /(.*)/g;
    $position2 = pos($java_file)+1;
    
    $byte_counter += $position2 - $position1;
  }  

  return $byte_counter;
} #end sub count_notes


###########################################################################
#vytiskne predany vysledek
# parametry: nazev souboru, full_path, cislo
sub print_result {

  
  my($files_name,$counts,$full_path) = @_;
  my (@files_name) = @$files_name;  #dereference na pole
  my (@counts) = @$counts;          #dereference na pole
  my @part_path;                    #pomocna promenna
  my $temp_name;                    #pomocna promenna
  my $length_line =0;               # delka casti s nazvem souboru
  my $i;                            #iterator v cyklu
  my $suma =0;                      #soucet hodnot
  my $format;
  my $temp_count;
  
  #udelame soucet
  foreach $temp_count (@counts) {
  	$suma +=  $temp_count; 
  }
   
  #jestli ma byt vypis zkraceny, odstranime cesty 
  if ($full_path == 0) {
    foreach $temp_name (@files_name) {
      @part_path = split("/",$temp_name);
      $temp_name = $part_path[$#part_path];
      #a zjistime optimalni delku radku vypisu
      if ($length_line < length($temp_name)) {
        $length_line = length($temp_name);
      }  
    }
  } else {
    #plna cesta, zjistime optimalni delku radku vypisu
    foreach $temp_name (@files_name) {
      
      $temp_name = cwd.substr($temp_name,1,length($temp_name));
      
      #pro vypis prevratime lomitka (windows)
      unless ($ENV{PWD}) {
      	$temp_name =~ s/\//\\/; 	
      }
      #hledame nejdelsi cestu a nazev
      if ($length_line < length($temp_name)) {
        $length_line = length($temp_name);
      }  
    }  
  }
    
  $length_line = $length_line + 2;   #jedna mezera mezi jmenem a cislem
  
  

  #tisk souboru a hodnot
  for ($i=0; $i <= $#files_name ; $i++) {
    
    #formatovany vypis
    $format = " "x($length_line - length($files_name[$i]));
    print $files_name[$i].$format;
    printf ("% ".(length($suma)+1)."d\n",$counts[$i]);
  }
  
  #tisk souctu
  $format = " "x($length_line - 7);
  print "Celkem:".$format;
  printf ("% ".length($suma)."d\n",$suma);
  
} #end sub print_result


###########################################################################
# odstrani koemntare a retezce
# parametr: retezec ke zpracovani
sub remove_comment_string {
  my($java_file) = @_;
  my $position1;
  my $position2;
  
  #odstraneni jednoradkove komentare
  $java_file =~ s/\/\/.*/ /g;
  
  #odstraneni viceradkoveho komentare \*\/
  while ($java_file =~ /(\/\*)/g) {
    $position1 = pos($java_file)-2;
    $java_file =~ /(\*\/)/g;
    $position2 = pos($java_file);
    substr($java_file,$position1,$position2 - $position1," ");
  }  

	#odstraneni retezce "
	while ($java_file =~ /(")/g) {
		$position1 = pos($java_file)-1;
		$java_file =~ /(")/g;
		$position2 = pos($java_file);
		substr($java_file,$position1,$position2 - $position1," ");
	}
	
	#odstraneni zapis znaku '
	while ($java_file =~ /(')/g) {
		$position1 = pos($java_file)-1;
		$java_file =~ /(')/g;
		$position2 = pos($java_file);
		substr($java_file,$position1,$position2 - $position1," ");
	}
	return $java_file;
}

###########################################################################
# prevedeme soubor na retezec a vratime
# parametr: cesta k souboru
sub file_to_string {
	my($file_name) = @_;
	my $java_file;
	my $line;

	
	#otevreme soubor
	open(SOURCE, "<".$file_name) or die "Nelze otevřít soubor: $!";
	
	#soubor do retezce v pameti
	while ($line = <SOURCE>){
		$line =~ s/\r//;
		$java_file .= $line;
	}
	
	close SOURCE;
	
	return $java_file;
	
} #end sub file_to_string 

###########################################################################
# Smaze ruzne integer, double a hexa zapisy cisel
# parametr: cesta k souboru
sub remove_numbers {
	my($java_file) = @_;
	

	#vselijake zapisy cisel float
	my $reg_exp = "\\b(\\d|0x|0X|0| \\.){1}([0123456789abcdefABCDEF\\.pPLl\\+-])"
			."+([0123456789abcdefABCDEF\\.pPLl])+";
	
	
	$java_file =~ s/$reg_exp/ /g;
	
	return $java_file;
}

###########################################################################
# vytiskne napovedu ke scriptu
sub print_help {
  print "
stats.pl
--------
Script dle zvolenych parametru provede textovou analyzu vsech souboru v tomto 
adresari a podadresarich.

Script funguje pod systemy GNU/Linux, FreeBSD (Perl 5.1 a novejsi ) a 
Microsoft Windows XP (ActivePerl 5.8 a novejsi).

Volitelne parametry:
--------------------

 --help vypise tuto napovedu.
 
 -k vypise pocet vsech klicovych slov v kazdem zdrojovem souboru a celkem. 
 Ignoruje poznamky a retezce. Klicova slova jsou dle oficialni dokumentace 
 k jazyku Java.

 -o vypise pocet operatoru (nikoliv oddelovacu apod.) v kazdem zdrojovem 
 souboru a celkem. Ignoruje poznamky a retezce. Operatory jsou dle oficialni 
 dokumentace k jazyku Java.

 -i vypise pocet identifikatoru v kazdem zdrojovem souboru a celkem.
 Nezahrnuje klicova slova. Ignoruje poznamky a retezce. Identifikatory jsou 
 dle oficialni dokumentace k jazyku Java.

 -ik vypise pocet identfikatoru v kazdem zdrojovem souboru a celkem. Zahrnuje
 klicova slova. Ignoruje poznamky a retezce.

 -w <pattern> vyhleda textovy retezec \"pattern\" ve vsech zdrojovych kodech a 
 vypise pocet vyskytu na soubor i celkem. Hleda i v poznamkach a retezcovych 
 literalech. Hledaji se NEPREKRYVAJICI se vyskyty vzoru <pattern>. Vyhledavani
 je case-sensitive.
 
 -c vypise celkovou delku poznamek v bytech na soubor a celkem. Poznamky pocita 
 vc. '/*', '*/', '//' a konce radku jako 1 byte (a to i u souboru s koncem 
 radku typu Windows/DOS).

 -p v kombinaci s predchozimi (az na --help) zpusobi, ze soubory se budou 
 vypisovat bez uplne cesty k souboru - ostatni parametry nelze vzajemne 
 kombinovat.
 
Format vystupu:
---------------
 <jmeno souboru, pripadne i s cestou><odsazeni><cislo>
 ....
 Celkem:														 <odsazeni><cislo>
 
 Cesta (volani bez parametru -p) se  nikdy nezkracuje, ani kdyz se nevejde do
 sirky terminalu (je to tak vhodnejsi pro presmerovani do souboru a dalsi 
 zpracovani). Vhodne odsazeni se dopocitava tak, aby cislo koncilo ve stejnem 
 sloupci pod sebou.

Priklad pouziti:
----------------
 ./stats.pl -c -p
 ./stats.pl -p -w XML
";
} #end sub print_help