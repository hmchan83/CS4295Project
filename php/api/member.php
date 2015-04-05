<?
class memberHandler{
	
	function main($para){
		switch($para['action']){
			case 'add':
				return $this->addMember($para['teamid'], $para['uid']);
			break;

			case 'get':
				return $this->getMembers($para['teamid']);
			break;

			case 'del':
				return $this->delMember($para['teamid'], $para['uid']);
			break;
		}
	}
	
	function addMember($teamid, $uid){
		$value = array();
		for($i=0;$i<count($uid);$i++){
			$value[] = '(\''.$uid[$i].'\',\''.$teamid.'\',0)';
		}
		$addsql = implode(",",$value);
		$arr=array();
		$sql = 'INSERT into team_user (`uid`, `teamid`,`isadmin`) VALUES '.$addsql;
		echo $sql;
		$query = $GLOBALS['mysqli']->query($sql);
		if(!$query){
			printf("Error: %s\n", $GLOBALS['mysqli']->error);
			$arr['result'] = 'false';
		}else{
			$arr['result'] = 'true';
			$handler = new GCMHandler();
			$handler -> sendAddtoTeamMsg($teamid,$uid);
		}
		return $arr;
	}

	function getMembers($teamid){
		$arr=array();
		$sql = 'SELECT `uid`,`username`,`name`,`tel` FROM `user` WHERE uid IN (SELECT uid from team_user where `teamid` = \''.$teamid.'\')';
		$query = $GLOBALS['mysqli']->query($sql);
		if(!$query){
			printf("Error: %s\n", $GLOBALS['mysqli']->error);
			$arr['result'] = 'false';
		}else{
			$members = array();
			while($result = $query->fetch_array(MYSQLI_ASSOC)){
				$members[]=$result;
			}
			$arr['users']=$members;
			$arr['result'] = 'true';
		}
		return $arr;
	}

	function delMember($teamid, $uid){
		$arr=array();
		$sql = 'DELETE from team_user where `teamid` = \''.$teamid.'\' and `uid` = \''.$uid.'\'';
		$query = $GLOBALS['mysqli']->query($sql);
		if(!$query){
			printf("Error: %s\n", $GLOBALS['mysqli']->error);
			$arr['result'] = 'false';
		}else{
			$arr['result'] = 'true';
		}
		return $arr;
	}

}
?>