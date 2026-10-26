/**
 * StayEase — Authentication & Session Manager
 */
const Auth = {
    currentUser: null,

    async init() {
        try {
            const res = await API.get('/auth/me');
            if (res && res.data) {
                this.currentUser = res.data;
            } else {
                this.currentUser = null;
            }
        } catch (e) {
            this.currentUser = null;
        }
        this.renderNavbar();
    },

    renderNavbar() {
        const navAuth = document.getElementById('navbarAuthSection');
        if (!navAuth) return;

        if (this.currentUser) {
            const isAdmin = this.currentUser.role === 'ROLE_ADMIN';
            navAuth.innerHTML = `
                <div class="dropdown">
                    <button class="btn btn-outline-gold dropdown-toggle d-flex align-items-center gap-2" type="button" data-bs-toggle="dropdown" aria-expanded="false">
                        <i class="bi bi-person-circle"></i>
                        <span>${this.currentUser.fullName}</span>
                        ${isAdmin ? '<span class="badge bg-warning text-dark ms-1">Admin</span>' : ''}
                    </button>
                    <ul class="dropdown-menu dropdown-menu-end shadow-lg border-0">
                        <li><h6 class="dropdown-header">${this.currentUser.email}</h6></li>
                        <li><a class="dropdown-item" href="/customer-dashboard.html"><i class="bi bi-calendar-check me-2"></i>My Bookings</a></li>
                        ${isAdmin ? '<li><a class="dropdown-item text-primary fw-bold" href="/admin-dashboard.html"><i class="bi bi-speedometer2 me-2"></i>Admin Dashboard</a></li>' : ''}
                        <li><hr class="dropdown-divider"></li>
                        <li><a class="dropdown-item text-danger" href="javascript:void(0)" onclick="Auth.logout()"><i class="bi bi-box-arrow-right me-2"></i>Sign Out</a></li>
                    </ul>
                </div>
            `;
        } else {
            navAuth.innerHTML = `
                <div class="d-flex align-items-center gap-2">
                    <a href="/login.html" class="btn btn-outline-light btn-sm px-3">Sign In</a>
                    <a href="/register.html" class="btn btn-gold btn-sm px-3">Register</a>
                </div>
            `;
        }
    },

    async login(email, password, redirectUrl = '/') {
        try {
            const res = await API.post('/auth/login', { email, password });
            API.showToast('Welcome back, ' + res.data.fullName + '!', 'success');
            setTimeout(() => {
                if (redirectUrl) {
                    window.location.href = redirectUrl;
                } else if (res.data.role === 'ROLE_ADMIN') {
                    window.location.href = '/admin-dashboard.html';
                } else {
                    window.location.href = '/';
                }
            }, 800);
        } catch (err) {
            API.showToast(err.message || 'Login failed. Please verify credentials.', 'danger');
        }
    },

    async register(fullName, email, password, phoneNumber) {
        try {
            await API.post('/auth/register', { fullName, email, password, phoneNumber });
            API.showToast('Account created successfully! Please sign in.', 'success');
            setTimeout(() => {
                window.location.href = '/login.html';
            }, 1200);
        } catch (err) {
            API.showToast(err.message || 'Registration failed.', 'danger');
        }
    },

    async logout() {
        try {
            await API.post('/auth/logout');
            API.showToast('Signed out successfully.', 'success');
            this.currentUser = null;
            setTimeout(() => {
                window.location.href = '/';
            }, 600);
        } catch (err) {
            window.location.href = '/';
        }
    },

    requireAuth() {
        if (!this.currentUser) {
            const currentPath = encodeURIComponent(window.location.pathname + window.location.search);
            window.location.href = `/login.html?redirect=${currentPath}`;
            return false;
        }
        return true;
    },

    requireAdmin() {
        if (!this.currentUser || this.currentUser.role !== 'ROLE_ADMIN') {
            API.showToast('Access Denied. Administrator credentials required.', 'danger');
            setTimeout(() => {
                window.location.href = '/';
            }, 1000);
            return false;
        }
        return true;
    }
};

document.addEventListener('DOMContentLoaded', () => {
    Auth.init();
});
