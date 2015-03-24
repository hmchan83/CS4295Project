<?
class memberHandler{
	
	function main($para){
		switch($para['action']){
			case 'add':
				return $this->addMember($para['teamid'], $para['uid']);
			break;

			case 'del':
				return $this->delMember($para['teamid'], $para['uid']);
			break;
		}
	}
	
	function addMember($teamid, $uid){
		$arr=array();
		$sql = 'INSERT into member (`uid`, `teamid`,`isadmin`) VALUES (\''.$uid.'\',\''.$teamid.'\',\'0\')';
		$query = $GLOBALS['mysqli']->query($sql);
		if(!$query){
			printf("Error: %s\n", $GLOBALS['mysqli']->error);
			$arr['result'] = 'false';
		}else{
			$arr['result'] = 'true';
		}
		return $arr;
	}

	function delMember($teamid, $uid){
		$arr=array();
		$sql = 'DELETE from member where `teamid` = \''.$teamid.'\' and `uid` = \''.$uid.'\'';
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