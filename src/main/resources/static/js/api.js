// ── Configuração base ──────────────────────────────────────────
const API_BASE = '/api';

function getToken() { return localStorage.getItem('olympia_token'); }
function getUser()  { return JSON.parse(localStorage.getItem('olympia_user') || 'null'); }
function isAdmin()  { const u = getUser(); return u && u.role === 'ROLE_ADMIN'; }
function isLogado() { return !!getToken(); }

function logout() {
    localStorage.removeItem('olympia_token');
    localStorage.removeItem('olympia_user');
    window.location.href = '../index.html';
}

// ── Fetch com JWT ──────────────────────────────────────────────
async function apiFetch(path, options = {}) {
    const token = getToken();
    const headers = { 'Content-Type': 'application/json', ...(options.headers || {}) };
    if (token) headers['Authorization'] = 'Bearer ' + token;

    const res = await fetch(API_BASE + path, { ...options, headers });
    const data = await res.json().catch(() => ({}));

    if (res.status === 401) { logout(); return; }
    if (!res.ok) throw new Error(data.mensagem || 'Erro na requisição');
    return data;
}

// ── Auth ───────────────────────────────────────────────────────
const Auth = {
    async login(email, senha) {
        const data = await apiFetch('/auth/login', {
            method: 'POST', body: JSON.stringify({ email, senha })
        });
        localStorage.setItem('olympia_token', data.dados.token);
        localStorage.setItem('olympia_user', JSON.stringify(data.dados));
        return data.dados;
    },
    async cadastrar(payload) {
        const data = await apiFetch('/auth/cadastro', {
            method: 'POST', body: JSON.stringify(payload)
        });
        localStorage.setItem('olympia_token', data.dados.token);
        localStorage.setItem('olympia_user', JSON.stringify(data.dados));
        return data.dados;
    }
};

// ── Pedidos ────────────────────────────────────────────────────
const Pedidos = {
    async criar(payload)       { return apiFetch('/pedidos', { method: 'POST', body: JSON.stringify(payload) }); },
    async listar()             { return apiFetch('/pedidos'); },
    async listarTodos()        { return apiFetch('/pedidos/todos'); },
    async buscar(id)           { return apiFetch('/pedidos/' + id); },
    async atualizarStatus(id, status) {
        return apiFetch(`/pedidos/${id}/status?status=${status}`, { method: 'PUT' });
    },
    async cancelar(id)         { return apiFetch('/pedidos/' + id, { method: 'DELETE' }); }
};

// ── Partituras ─────────────────────────────────────────────────
const Partituras = {
    async listar()             { return apiFetch('/partituras'); },
    async criar(payload)       { return apiFetch('/partituras', { method: 'POST', body: JSON.stringify(payload) }); },
    async atualizar(id, payload){ return apiFetch('/partituras/' + id, { method: 'PUT', body: JSON.stringify(payload) }); },
    async deletar(id)          { return apiFetch('/partituras/' + id, { method: 'DELETE' }); }
};

// ── Formações ──────────────────────────────────────────────────
const Formacoes = {
    async listar() { return apiFetch('/formacoes'); }
};

// ── Histórico ──────────────────────────────────────────────────
const Historico = {
    async listar()      { return apiFetch('/historico'); },
    async porPedido(id) { return apiFetch('/historico/pedido/' + id); }
};

// ── Helpers UI ─────────────────────────────────────────────────
function mostrarAlerta(el, msg, tipo = 'success') {
    el.innerHTML = `<div class="alert alert-${tipo}">${msg}</div>`;
    setTimeout(() => el.innerHTML = '', 4000);
}

function badgeStatus(status) {
    const map = { PENDENTE:'pendente', CONFIRMADO:'confirmado', CANCELADO:'cancelado', CONCLUIDO:'concluido' };
    return `<span class="badge badge-${map[status]||'pendente'}">${status}</span>`;
}

function formatarMoeda(v) {
    return new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(v);
}

function formatarData(d) {
    if (!d) return '-';
    return new Date(d + 'T00:00:00').toLocaleDateString('pt-BR');
}

// Redireciona para login se não estiver logado
function requireLogin() {
    if (!isLogado()) { window.location.href = 'orcamento.html'; return false; }
    return true;
}

// Monta nav com estado de login
function renderNav(navId = 'nav-auth') {
    const el = document.getElementById(navId);
    if (!el) return;
    if (isLogado()) {
        const u = getUser();
        el.innerHTML = `
            <a href="pedidos.html">Meus Pedidos</a>
            ${isAdmin() ? '<a href="admin.html">Admin</a>' : ''}
            <a href="#" onclick="logout()">Sair (${u.nome.split(' ')[0]})</a>
        `;
    } else {
        el.innerHTML = `<a href="orcamento.html">Login</a>`;
    }
}
