'use client';

import { useEffect, useState } from 'react';
import { getInitialLanguage, Lang, translations } from '@/lib/i18n';

export function useLanguage() {
  const [lang, setLang] = useState<Lang>('sw');
  useEffect(() => {
    setLang(getInitialLanguage());
    const handler = (event: Event) => setLang((event as CustomEvent<Lang>).detail);
    window.addEventListener('jmk-language-change', handler);
    return () => window.removeEventListener('jmk-language-change', handler);
  }, []);
  return { lang, t: translations[lang] };
}
