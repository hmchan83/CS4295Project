<?php
require './api/login.php';
require './api/team.php';

$dbhost = "localhost";
$dbuser = "";
$dbpwd = "";
$db = "CS4295";

@$GLOBALS['mysqli'] =  new mysqli($dbhost, $dbuser, $dbpwd, $db);

$request = $_GET['request'];
$para = requestDecode($request);
//print_r($para);
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