import { NextResponse } from 'next/server';
import type { NextRequest } from 'next/server';

export function middleware(request: NextRequest) {
  const { pathname } = request.nextUrl;
  const token = request.cookies.get('token')?.value;

  // Public routes - hazihitaji login
  const publicRoutes = ['/landing', '/loan-guide', '/login', '/register', '/forgot-password', '/reset-password'];
  const isPublic = publicRoutes.some((route) => pathname.startsWith(route));

  // Protected routes - zinahitaji login
  const protectedRoutes = ['/admin', '/dashboard', '/borrower', '/lender', '/bursar', '/management', '/payments', '/profile'];
  const isProtected = protectedRoutes.some((route) => pathname.startsWith(route));

  if (isProtected && !token) {
    return NextResponse.redirect(new URL('/login', request.url));
  }

  // Kama ameingia, asirudi kwenye login/register
  if ((pathname === '/login' || pathname === '/register') && token) {
    return NextResponse.redirect(new URL('/dashboard', request.url));
  }

  return NextResponse.next();
}

export const config = {
  matcher: ['/((?!api|_next/static|_next/image|favicon.ico).*)'],
};
