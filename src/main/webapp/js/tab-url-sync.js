export function initTabUrlSync() {
    const tablists = document.querySelectorAll('[data-url-sync-tabs]');
    if (!tablists.length) return;

    const ctx = getContextPath();

    const syncAll = () => {
        tablists.forEach((tablist) => syncOneFromUrl(tablist, ctx));
    };

    tablists.forEach((tablist) => wireTablist(tablist, ctx));
    window.addEventListener('popstate', syncAll);
    syncAll();
}

function wireTablist(tablist, ctx) {
    const triggers = tablist.querySelectorAll('[data-bs-toggle="tab"]');
    triggers.forEach((el) => {
        el.addEventListener('shown.bs.tab', function () {
            const url = normalizeUrl(this.getAttribute('data-tab-url'), ctx);
            if (!url) return;
            if (window.location.pathname === url) return;
            window.history.pushState({ tabId: this.id || null }, '', url);
        });
    });
}

function syncOneFromUrl(tablist, ctx) {
    const pathname = (window.location.pathname || '').toLowerCase();
    const triggers = Array.from(tablist.querySelectorAll('[data-bs-toggle="tab"]'));
    if (!triggers.length) return;

    const match = triggers.find((el) => matchesPath(el, pathname, ctx)) || triggers[0];
    if (!match) return;

    if (window.bootstrap?.Tab) {
        window.bootstrap.Tab.getOrCreateInstance(match).show();
    }
}

function matchesPath(el, pathname, ctx) {
    const url = normalizeUrl(el.getAttribute('data-tab-url'), ctx);
    const matchAttr = el.getAttribute('data-tab-match');
    const matchList = (matchAttr ? matchAttr.split(',') : []).map((s) => s.trim()).filter(Boolean);

    // Always match its own URL
    const candidates = [url, ...matchList.map((m) => normalizeUrl(m, ctx))].filter(Boolean);
    return candidates.some((c) => pathname === c.toLowerCase());
}

function normalizeUrl(raw, ctx) {
    if (!raw) return null;
    const trimmed = raw.trim();
    if (!trimmed) return null;
    if (!trimmed.startsWith('/')) return null;
    return ctx === '/' ? trimmed : `${ctx}${trimmed}`;
}

function getContextPath() {
    const p = window.location.pathname || '';
    const idx = p.indexOf('/', 1);
    if (idx <= 0) return '';

    const first = p.substring(0, idx);

    if (first === '/profile' || first === '/login' || first === '/home') return '';
    return first;
}

