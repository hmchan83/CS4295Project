<?
class teamHandler{
	
	function main($para){
		switch($para['action']){
			case 'get':
				return $this->getTeams($para['uid']);
			break;
			
			case 'new':
				return $this->newTeam($para['uid'],$para['name'],$para['desrc']);
			break;

			case 'del':
				return $this->delTeam($para['teamid']);
			break;
		}
	}
	
	function getTeams($uid){
		$arr=array();
		$query = $GLOBALS['mysqli']->query('SELECT * FROM team WHERE teamid IN (SELECT teamid FROM team_user WHERE uid=\''.$uid.'\')');
		if(!$query){
			printf("Error: %s\n", $GLOBALS['mysqli']->error);
		}else{
			$team = array();
			while($result = $query->fetch_array(MYSQLI_ASSOC)){
				$team[]=$result;
			}
			$arr['teams']=$team;
		}
		return $arr;
	}
	
	function newTeam($uid,$name,$desrc){
		$arr=array();
		$sql = 'INSERT into team (`teamname`, `desrc`) VALUES (\''.$name.'\',\''.$desrc.'\')';
		$query = $GLOBALS['mysqli']->query($sql);
		$sql = 'INSERT into team_user (`uid`, `teamid`,`isadmin`) VALUES (\''.$uid.'\',\''.$GLOBALS['mysqli']->insert_id.'\',\'1\')';
		$query2 = $GLOBALS['mysqli']->query($sql);
		if(!$query){
			printf("Error: %s\n", $GLOBALS['mysqli']->error);
			$arr['result'] = 'false';
		}else{
			$arr['result'] = 'true';
		}
		return $arr;	
	}

	function delTeam($teamid){
		$arr=array();
		$sql = 'DELETE from team where `teamid` = \''.$teamid.'\'';
		$query = $GLOBALS['mysqli']->query($sql);
		$sql = 'DELETE from team_user where `teamid` = \''.$teamid.'\'';
		$query2 = $GLOBALS['mysqli']->query($sql);
		$sql = 'DELETE from message where `teamid` = \''.$teamid.'\'';
		$query3 = $GLOBALS['mysqli']->query($sql);
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