<?php
require './api/login.php';
require './api/team.php';
require './api/member.php';
require './api/message.php';

date_default_timezone_set('Asia/Hong_Kong');

$dbhost = "localhost";
$dbuser = "";
$dbpwd = "";
$db = "justforgra_CS4295";

$GLOBALS['mysqli'] =  new mysqli($dbhost, $dbuser, $dbpwd, $db);

$para = array();
foreach ($_GET as $key => $value){
	$para[$key] = $value;	
}
$json = file_get_contents("php://input");
//echo $json;
$array = json_decode($json, true);
//var_dump($array);
if(count($array)>0)
foreach ($array as $key => $value){
	$para[$key] = $value;
}
$response='';

switch($para['handler']){
    case 'login':
        $handler = new loginHandler();
        $response = $handler->main($para);
        break;

    case 'team':
        $handler = new teamHandler();
        $response = $handler->main($para);
        break;

    case 'member':
        $handler = new memberHandler();
        $response = $handler->main($para);
        break;

    case 'message':
        $handler = new messageHandler();
        $response = $handler->main($para);
        break;
}
/*
switch($para['action']){
	case 'login':
	$handler = new loginHandler();
	$response = $handler->login($para['username'],$para['password']);
	break;
	case 'getTeam':
	$handler = new groupHandler();
	$response = $handler->getGroup($para['username']);
}*/
echo json_encode($response);


function requestDecode($str){
    $arr = array();
    $request = base64_decode($str);
    $temp = explode('&',$request);
    foreach($temp as $var){
        $value = explode('=',$var);
        $arr[$value[0]] = $value[1];
    }
    return $arr;
}
?>