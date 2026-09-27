import { create } from 'zustand';
import { persist } from 'zustand/middleware';

interface User { userId: number; fullName: string; email: string; role: string; }
interface AuthState {
  user: User | null;
  token: string | null;
  isAuthenticated: boolean;
  setAuth: (token: string, user: User) => void;
  logout: () => void;
}

export const useAuth = create<AuthState>()(persist((set) => ({
  user: null,
  token: null,
  isAuthenticated: false,
  setAuth: (token, user) => set({ token, user, isAuthenticated: true }),
  logout: () => set({ token: null, user: null, isAuthenticated: false }),
}), { name: 'jmkloanapp-auth' }));
