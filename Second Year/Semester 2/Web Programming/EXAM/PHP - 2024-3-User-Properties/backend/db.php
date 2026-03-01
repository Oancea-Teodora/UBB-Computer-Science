<?php
function getPDO() 
{
    return new PDO('mysql:host=127.0.0.1;port=3307;dbname=2024-3;charset=utf8mb4',
      'root','', 
      [PDO::ATTR_ERRMODE=>PDO::ERRMODE_EXCEPTION]);
}