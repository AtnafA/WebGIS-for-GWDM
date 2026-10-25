var currentDate = new Date();
var dayname=currentDate.getDay();
if(dayname==1){
  dayname="Monday"  
}
else if(dayname==2){
 dayname="Teusday"     
}
else if(dayname==3){
 dayname="Wednesday"     
}
else if(dayname==4){
 dayname="Thrusday"     
}
else if(dayname==5){
 dayname="Friday"     
}
else if(dayname==6){
 dayname="Saturday"     
}
else if(dayname==0){
 dayname="Sunday"     
}
var day = currentDate.getDate();
var month = currentDate.getMonth() + 1;
var year = currentDate.getFullYear();
var link = "https://eec.com.et";
var img = "dist/img/Icon11.png";
document.write('<center><p>&copy;  '+dayname+", "+day + "/" + month + "/" + year+', All Right Reserved By AAWSA <b>|</b> Powered By '
+'<a href="'+link+'" target="_blank"><img src="'+img+'"></a> <br>GWDMS</p></center>');