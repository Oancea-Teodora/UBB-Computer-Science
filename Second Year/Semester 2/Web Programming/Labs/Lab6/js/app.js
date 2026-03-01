// js/app.js
document.addEventListener('DOMContentLoaded',()=>{
    const genreSelect = document.getElementById('genre-select');
    const fileList    = document.getElementById('file-list');
    const filterText  = document.getElementById('current-filter').querySelector('strong');
  
    function loadList(genre){
      filterText.textContent = genre || 'All genres';
      // clear list
      fileList.innerHTML = '';
      // fetch via AJAX
      fetch(`api.php?action=browse&genre=${encodeURIComponent(genre)}`)
        .then(r=>r.json())
        .then(data=>{
          if(!data.length){
            fileList.innerHTML = '<li><em>No files found.</em></li>';
          } else {
            data.forEach(item=>{
              let li = document.createElement('li');
              // clickable to edit or delete:
              li.innerHTML = `
                ${item.title}
                [<a href="edit.php?id=${item.id}">Edit</a>]
                [<a href="delete.php?id=${item.id}">Delete</a>]
              `;
              fileList.appendChild(li);
            });
          }
        });
    }
  
    genreSelect.addEventListener('change', ()=> {
      loadList(genreSelect.value);
    });
  
    // initial load
    loadList('');
  });
  