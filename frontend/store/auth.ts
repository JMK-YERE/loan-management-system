import { create } from 'zustand';
import { persist } from 'zustand/middleware';

interface User {
  userId: number;
  fullName: string;
  email: string;
  role: string;
}

interface AuthState {
  user: User | null;
  token: string | null;
  isAuthenticated: boolean;
  setAuth: (token: string, user: User) => void;
  logout: () => void;
}

export const useAuth = create<AuthState>()(
  persist(
    (set) => ({
      user: null,
      token: null,
      isAuthenticated: false,

      setAuth: (token, user) => {
        if (typeof window !== 'undefined') {
          localStorage.setItem('token', token);
          localStorage.setItem('user', JSON.stringify(user));
          document.cookie = `token=${token}; path=/; max-age=86400; SameSite=Lax`;
        }
        set({ token, user, isAuthenticated: true });
      },

      logout: () => {
        if (typeof window !== 'undefined') {
          localStorage.removeItem('token');
          localStorage.removeItem('user');
          localStorage.removeItem('jmkloanapp-auth');
          document.cookie = 'token=; path=/; max-age=0; SameSite=Lax';
        }
        set({ token: null, user: null, isAuthenticated: false });
      },
    }),
    { name: 'jmkloanapp-auth' }
  )
);
