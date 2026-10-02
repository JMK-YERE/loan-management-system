import { NextResponse } from 'next/server';
import type { NextRequest } from 'next/server';

export function middleware(request: NextRequest) {
  const { pathname } = request.nextUrl;
  const token = request.cookies.get('token')?.value;

  // Public routes. Login/register must ALWAYS remain reachable so an existing
  // session never silently bypasses the credential screen.
  const publicRoutes = [
    '/',
    '/landing',
    '/loan-guide',
    '/login',
    '/register',
    '/forgot-password',
    '/reset-password',
  ];
  const isPublic = publicRoutes.some((route) => pathname === route || pathname.startsWith(route + '/'));

  const protectedRoutes = [
    '/admin',
    '/dashboard',
    '/borrower',
    '/lender',
    '/bursar',
    '/management',
    '/payments',
    '/profile',
    '/agreements',
    '/signatures',
  ];
  const isProtected = protectedRoutes.some((route) => pathname === route || pathname.startsWith(route + '/'));

  if (isProtected && !token) {
    const url = request.nextUrl.clone();
    url.pathname = '/login';
    url.searchParams.set('next', pathname);
    return NextResponse.redirect(url);
  }

  // Do NOT redirect /login or /register merely because a stale/old token exists.
  // The user must explicitly authenticate again when they choose Login.
  return NextResponse.next();
}

export const config = {
  matcher: ['/((?!api|_next/static|_next/image|favicon.ico).*)'],
};
