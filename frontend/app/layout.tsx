import type { Metadata } from 'next';
import { Inter } from 'next/font/google';
import './globals.css';

const inter = Inter({ subsets: ['latin'] });

export const metadata: Metadata = {
  title: 'JmkLoanApp - Mikopo kwa Wote Tanzania',
  description: 'Mfumo wa kisasa wa mikopo kwa wakopeshaji binafsi, VICOBA, SACCOS Tanzania.',
};

export default function RootLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <html lang="sw" suppressHydrationWarning>
      <body className={inter.className}>{children}</body>
    </html>
  );
}
