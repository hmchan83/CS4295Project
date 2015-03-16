<?
class teamHandler{
	
	function main($para){
		switch($para['action']){
			case 'get':
				return $this->getTeam($para['uid']);
			break;
			
			case 'new':
				return $this->newTeam($para['uid'],$para['name'],$para['desrc']);
			break;
		}
	}
	
	function getTeam($uid){
		$arr=array();
		$query = $GLOBALS['mysqli']->query('SELECT * FROM team WHERE teamid IN (SELECT teamid FROM member WHERE uid=\''.$uid.'\')');
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
		$sql = 'INSERT into team (`teamname`, `desrc`) VALUES (\''.$name.'\',\''.$desrc.'\')';
		$query = $GLOBALS['mysqli']->query($sql);
		$sql = 'INSERT into member (`uid`, `teamid`,`isadmin`) VALUES (\''.$uid.'\',\''.$GLOBALS['mysqli']->insert_id.'\',\'1\')';
		$query2 = $GLOBALS['mysqli']->query($sql);
		return $this->getTeam($uid);
	}
}
?>