<?
class loginHandler{
	
	function main($para){
		switch($para['action']){
			case 'login':
				return $this->login($para['username'],$para['password']);
			break;			
		}
	}

	function login($username,$password){
		$arr=array();
		$query = $GLOBALS['mysqli']->query('SELECT COUNT(*) FROM user WHERE username = \''.$username.'\' AND password = \''.$password.'\'');
		if(!$query){
			printf("Error: %s\n", $GLOBALS['mysqli']->error);
		}
		$result = $query->fetch_array(MYSQLI_BOTH);
		if($result[0]>0){
			$arr['result']='True';
			$query = $GLOBALS['mysqli']->query('SELECT uid,username FROM user WHERE username = \''.$username.'\' AND password = \''.$password.'\'');
			$results = $query->fetch_array(MYSQLI_ASSOC);
			foreach($results as $key=>$var){
				$arr[$key]=$var;	
			}
		}else{
			
			$arr['result']='False';
		}
		return $arr;
	}
}
?>