import type { Metadata } from 'next';
import './globals.css';

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
      <body>{children}</body>
    </html>
  );
}
