<?php

require __DIR__.'/init.php';

?>

<html>

<label>Search Player: </label>
<input type="text" id="search" onkeyup="searchPlayers()" />
<ul id="results"></ul>


<button onclick="FirstDegree()">First Degree</button>
<ul id="first-degree"></ul>

<button onclick="SecondDegree()">Second Degree</button>
<ul id="second-degree"></ul>

<button onclick="ThirdDegree()">Third Degree</button>
<ul id="third-degree"></ul>

<script>
function searchPlayers() {
  const term = document.getElementById("search").value;

  fetch("../backend/search.php?q=" + term)
    .then(response => response.json())
    .then(data => {

      data.sort((a, b) => a.localeCompare(b));

      const list = document.getElementById("results");
      list.innerHTML = "";

      for (let i = 0; i < data.length; i++) {
        const li = document.createElement("li");
        li.textContent = data[i];
        list.appendChild(li);
      }
    });  
}

function FirstDegree() {

  fetch("../backend/degrees.php")
    .then(response => response.json())
    .then(data => {

      const list = document.getElementById("first-degree");
      list.innerHTML = "";

      for (let i = 0; i < data.length; i++) {
        const li = document.createElement("li");
        li.textContent = data[i];
        list.appendChild(li);
      }
    });  
}

function SecondDegree() {

  fetch("../backend/seconddegrees.php")
    .then(response => response.json())
    .then(data => {

      const list = document.getElementById("second-degree");
      list.innerHTML = "";

      for (let i = 0; i < data.length; i++) {
        const li = document.createElement("li");
        li.textContent = data[i];
        list.appendChild(li);
      }
    });  
}

function ThirdDegree() {

  fetch("../backend/thirddegrees.php")
    .then(response => response.json())
    .then(data => {

      const list = document.getElementById("third-degree");
      list.innerHTML = "";

      for (let i = 0; i < data.length; i++) {
        const li = document.createElement("li");
        li.textContent = data[i];
        list.appendChild(li);
      }
    });  
}

</script>




</html>