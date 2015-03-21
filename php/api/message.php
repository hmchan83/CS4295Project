<?
class messageHandler{
	
	function main($para){
		switch($para['action']){
			case 'add':
				return $this->addMessage($para['replyid'], $para['teamid'], $para['uid'], $para['msg']);
			break;

			case 'get':
				return $this->getMessage($para['teamid'], $para['start'], $para['rows']);
			break;
		}
	}
	
	function addMessage($replyid, $teamid, $uid, $msg){
		$arr=array();
		$sql = 'INSERT into message (`replyid`, `teamid`,`uid`,`msg`,`timestamp`) VALUES (\''.$replyid.'\',\''.$teamid.'\', \''.$uid.'\', \''.$msg.'\', NOW())';
		$query = $GLOBALS['mysqli']->query($sql);
		if(!$query){
			printf("Error: %s\n", $GLOBALS['mysqli']->error);
		}else{
			$arr['result'] = 'true';
		}
		return $arr;
	}

	function getMessage($teamid, $start, $rows){
		$arr=array();
		$sql = 'SELECT * from message where `teamid`=\''.$teamid.'\' LIMIT '.$start.', '.$rows;
		$query = $GLOBALS['mysqli']->query($sql);
		if(!$query){
			printf("Error: %s\n", $GLOBALS['mysqli']->error);
		}else{
			$msg = array();
			while($result = $query->fetch_array(MYSQLI_ASSOC)){
				$msg[]=$result;
			}
			$arr['msg']=$msg;
		}
		return $arr;
	}

}
?>