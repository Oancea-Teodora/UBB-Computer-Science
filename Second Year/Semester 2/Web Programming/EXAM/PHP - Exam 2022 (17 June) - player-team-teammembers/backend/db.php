<?php
function getPDO() 
{
    return new PDO('mysql:host=127.0.0.1;port=3307;dbname=team_members;charset=utf8mb4',
      'root','', 
      [PDO::ATTR_ERRMODE=>PDO::ERRMODE_EXCEPTION]);
}