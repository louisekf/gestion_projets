// ================================================================
// GESTION PROJETS - Main JS
// ================================================================

document.addEventListener('DOMContentLoaded', () => {

    // ---- Auto-dismiss alerts ----
    document.querySelectorAll('.alert[data-auto-dismiss]').forEach(alert => {
        setTimeout(() => {
            alert.style.opacity = '0';
            alert.style.transform = 'translateY(-8px)';
            setTimeout(() => alert.remove(), 300);
        }, 4000);
    });

    // ---- Confirm delete modals ----
    document.querySelectorAll('[data-confirm]').forEach(btn => {
        btn.addEventListener('click', e => {
            const msg = btn.dataset.confirm || 'Confirmer cette action ?';
            if (!confirm(msg)) e.preventDefault();
        });
    });

    // ---- Search filter (client-side) ----
    const searchInput = document.getElementById('searchInput');
    const filterStatut = document.getElementById('filterStatut');
    const filterDomaine = document.getElementById('filterDomaine');

    function applyFilters() {
        const query   = (searchInput?.value || '').toLowerCase();
        const statut  = (filterStatut?.value || '').toLowerCase();
        const domaine = (filterDomaine?.value || '').toLowerCase();

        document.querySelectorAll('[data-searchable]').forEach(row => {
            const text     = row.textContent.toLowerCase();
            const rowStatut  = (row.dataset.statut  || '').toLowerCase();
            const rowDomaine = (row.dataset.domaine || '').toLowerCase();

            const matchText    = !query   || text.includes(query);
            const matchStatut  = !statut  || rowStatut === statut;
            const matchDomaine = !domaine || rowDomaine.includes(domaine);

            row.style.display = (matchText && matchStatut && matchDomaine) ? '' : 'none';
        });
    }

    searchInput?.addEventListener('input', applyFilters);
    filterStatut?.addEventListener('change', applyFilters);
    filterDomaine?.addEventListener('change', applyFilters);

    // ---- Modals ----
    document.querySelectorAll('[data-modal-open]').forEach(btn => {
        btn.addEventListener('click', () => {
            const id = btn.dataset.modalOpen;
            document.getElementById(id)?.classList.add('active');
        });
    });

    document.querySelectorAll('[data-modal-close]').forEach(btn => {
        btn.addEventListener('click', () => {
            btn.closest('.modal-overlay')?.classList.remove('active');
        });
    });

    document.querySelectorAll('.modal-overlay').forEach(overlay => {
        overlay.addEventListener('click', e => {
            if (e.target === overlay) overlay.classList.remove('active');
        });
    });

    // ---- File drop zone ----
    const dropZone = document.getElementById('dropZone');
    const fileInput = document.getElementById('csvFile');

    if (dropZone && fileInput) {
        dropZone.addEventListener('click', () => fileInput.click());

        dropZone.addEventListener('dragover', e => {
            e.preventDefault();
            dropZone.classList.add('dragging');
        });

        dropZone.addEventListener('dragleave', () => dropZone.classList.remove('dragging'));

        dropZone.addEventListener('drop', e => {
            e.preventDefault();
            dropZone.classList.remove('dragging');
            const file = e.dataTransfer.files[0];
            if (file) handleFileSelect(file);
        });

        fileInput.addEventListener('change', () => {
            if (fileInput.files[0]) handleFileSelect(fileInput.files[0]);
        });

        function handleFileSelect(file) {
            const nameEl = document.getElementById('fileName');
            const sizeEl = document.getElementById('fileSize');
            const preview = document.getElementById('filePreview');
            if (nameEl) nameEl.textContent = file.name;
            if (sizeEl) sizeEl.textContent = formatBytes(file.size);
            if (preview) preview.style.display = '';
            dropZone.querySelector('.file-drop-text').textContent = file.name;
        }
    }

    function formatBytes(bytes) {
        if (bytes < 1024) return bytes + ' B';
        if (bytes < 1048576) return (bytes / 1024).toFixed(1) + ' KB';
        return (bytes / 1048576).toFixed(1) + ' MB';
    }

    // ---- Avancement slider live update ----
    const avancementInput = document.getElementById('niveauAvancement');
    const avancementLabel = document.getElementById('avancementLabel');
    if (avancementInput && avancementLabel) {
        const update = () => {
            avancementLabel.textContent = avancementInput.value + '%';
        };
        update();
        avancementInput.addEventListener('input', update);
    }

    // ---- Mobile sidebar toggle ----
    const menuToggle = document.getElementById('menuToggle');
    const sidebar = document.querySelector('.sidebar');
    menuToggle?.addEventListener('click', () => {
        sidebar?.classList.toggle('open');
    });

    // ---- Fade in page content ----
    document.querySelector('.page-content')?.classList.add('fade-in');
});
