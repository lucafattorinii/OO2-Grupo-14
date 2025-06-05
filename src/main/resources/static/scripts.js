document.addEventListener('DOMContentLoaded', () => {
  // ─── 1) Menú Hamburguesa ─────────────────────────────
  const menu = document.getElementById('sideMenu'),
        body = document.getElementById('body');
  window.toggleMenu = () => {
    if (menu.style.left === '0px') {
      menu.style.left = '-250px';
      body.classList.remove('shift');
    } else {
      menu.style.left = '0px';
      body.classList.add('shift');
    }
  };

  // ─── 2) Crear Cliente ─────────────────────────────────
  const btnShowCreate   = document.getElementById('show-create-btn'),
        boxCreate       = document.getElementById('create-container'),
        btnCancelCreate = document.getElementById('cancel-create-btn');
  if (btnShowCreate && boxCreate && btnCancelCreate) {
    btnShowCreate.addEventListener('click', () => {
      boxCreate.style.display     = 'block';
      btnShowCreate.style.display = 'none';
    });
    btnCancelCreate.addEventListener('click', () => {
      boxCreate.style.display     = 'none';
      btnShowCreate.style.display = 'inline-block';
    });
  }

  // ─── 3) Eliminar Cliente ──────────────────────────────
  const btnShowDelete   = document.getElementById('show-delete-btn'),
        boxDelete       = document.getElementById('delete-container'),
        btnCancelDelete = document.getElementById('cancel-delete-btn');
  if (btnShowDelete && boxDelete && btnCancelDelete) {
    btnShowDelete.addEventListener('click', () => {
      boxDelete.style.display      = 'block';
      btnShowDelete.style.display  = 'none';
    });
    btnCancelDelete.addEventListener('click', () => {
      boxDelete.style.display      = 'none';
      btnShowDelete.style.display  = 'inline-block';
    });
  }

  // ─── 4) Modificar Cliente ──────────────────────────────
  const btnShowUpdate   = document.getElementById('show-update-btn'),
        boxUpdate       = document.getElementById('update-container'),
        btnCancelUpdate = document.getElementById('cancel-update-btn');
  if (btnShowUpdate && boxUpdate && btnCancelUpdate) {
    btnShowUpdate.addEventListener('click', () => {
      boxUpdate.style.display      = 'block';
      btnShowUpdate.style.display  = 'none';
    });
    btnCancelUpdate.addEventListener('click', () => {
      boxUpdate.style.display      = 'none';
      btnShowUpdate.style.display  = 'inline-block';
    });
  }
});
