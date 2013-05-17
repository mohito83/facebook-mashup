<?php

header("Content-type: text/xml; charset=UTF-8");

function processEntry($arg1){
preg_match_all("/src=(.*?)height/",$arg1,$img,PREG_PATTERN_ORDER);
$img_str="".$img[1][0];

preg_match_all("/<\/span>[\s\S]*?<a href=\"\/title\/[\s\S]*?>(.*?)<\/a>/",$arg1,$ttle,PREG_PATTERN_ORDER);
$ttle_txt="".$ttle[1][0];

preg_match_all("/<span class=\"year_type\">(.*?)<\/span>/",$arg1,$year,PREG_PATTERN_ORDER);
$int_years=preg_replace('/([^0-9\\.])/i', '',$year[1][0]);
$year_txt="".$int_years;

preg_match_all("/Dir:[\s\S]*?<a[\s\S]*?(.*?)[\s\S]*?With:/",$arg1,$dir,PREG_SET_ORDER);
if(count($dir)==0){
$dir_txt="NA";
}else{
preg_match_all("/>(.*?)<\/a>*/",$dir[0][0],$dirs,PREG_SET_ORDER);
$count=0;
$dir_txt="";
while($count<count($dirs)){
$dir_txt=$dir_txt."".$dirs[$count][1].",";
$count++;
}
}

preg_match_all("/Users rated this[\s\S]*?(.*?)\/10/",$arg1,$rate,PREG_SET_ORDER);
if(count($rate)==0){
$rate_txt="NA";
}else{
$rate_txt="".$rate[0][1];
}

preg_match_all("/<\/span>[\s\S]*?<a href=\"[\s\S]*?(.*?)\">/",$arg1,$details,PREG_SET_ORDER);
if(count($details)==0){
$det_txt="NA";
}else{
$det_txt="".$details[0][1];
}
$det_txt="http://imdb.com".$details[0][1];

$xml_row="<result cover=".$img_str." title=\"".$ttle_txt."\" year=\"".$year_txt."\" director=\"".$dir_txt."\" rating=\"".$rate_txt."\" details=\"".$det_txt."\"/>";
echo $xml_row;
}


function parseSearchResult($arg1){

preg_match_all("/<tr[\s\S]*?<\/tr>/",$arg1,$trs,PREG_PATTERN_ORDER);
echo "<results total=\"".(count($trs[0])-1)."\">";
$counter =1;
while($counter<count($trs[0])){
processEntry($trs[0][$counter]);
$counter=$counter+1;
}
echo "</results>";
}

$title=$_GET["Title"];
$errorType="";
$mType="";

if($_GET["mediaType"]=="Video Game"){
$errorType="video games";
$mType="game";
}
else if($_GET["mediaType"]=="TV Series"){
$errorType="tv series";
$mType="tv_series";
}
else if($_GET["mediaType"]=="Feature Film"){
$errorType="movies";
$mType="feature";
}else{
$errorType="movies or tv series or video games";
$mType="feature,tv_series,game";
}

$title=urlencode($title);
$url="http://www.imdb.com/search/title?title=$title&title_type=$mType";

$search_result=file_get_contents($url);

echo "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";

preg_match_all("/<table class=\"results\">[\s\S]*?<\/table>/",$search_result,$result_set,PREG_SET_ORDER);
if(count($result_set)==0){
echo "<rsp stat=\"failed\">";
} else{
echo "<rsp stat=\"ok\">";
parseSearchResult("".$result_set[0][0]);
}
echo "</rsp>";
?>

