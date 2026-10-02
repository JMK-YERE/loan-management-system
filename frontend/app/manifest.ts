import type { MetadataRoute } from 'next';

export default function manifest(): MetadataRoute.Manifest {
  return {
    name: 'JmkLoanApp',
    short_name: 'JmkLoan',
    description: 'Mfumo wa mikopo na financial operations',
    start_url: '/login',
    display: 'standalone',
    background_color: '#f8fafc',
    theme_color: '#2563eb',
    lang: 'sw',
    orientation: 'portrait-primary'
  };
}