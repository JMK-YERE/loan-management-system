import type { Metadata } from 'next';
import './globals.css';
import GlobalTranslator from '@/components/GlobalTranslator';

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
      <body><GlobalTranslator />{children}</body>
    </html>
  );
}
