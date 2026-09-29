export type Lang = 'sw' | 'en';

export const translations = {
  sw: {
    nav: { features:'Vipengele', how:'Jinsi inavyofanya kazi', security:'Usalama', contact:'Mawasiliano', login:'Ingia', register:'Jisajili' },
    hero: { badge:'MFUMO WA MIKOPO', title:'Simamia mikopo kwa urahisi, usalama na uwazi.', text:'JmkLoanApp inaunganisha wakopaji, wakopeshaji na wadhamini katika mfumo mmoja wa kisasa wenye ufuatiliaji wa mikopo na malipo.', primary:'Anza sasa', secondary:'Ingia kwenye akaunti' },
    trust: ['JWT authentication','Role-based access','Payment tracking','Responsive design'],
    featuresTitle:'Kila kitu muhimu, sehemu moja',
    featuresSub:'Zana zilizojengwa kwa ajili ya usimamizi wa mikopo wa kila siku.',
    features:[
      ['Calculator','Kokotoa marejesho','Hesabu riba, ada na makadirio ya malipo kabla ya kuunda mkopo.'],
      ['Workflow','Mtiririko wa mkopo','Unda, kagua, idhinisha, toa na fuatilia hali ya mkopo.'],
      ['Payments','Malipo','Rekodi malipo na fuatilia historia, salio na uthibitisho.'],
      ['Guarantor','Wadhamini','Simamia maombi ya udhamini na maamuzi yao kwa usalama.'],
      ['Security','Usalama','JWT, role-based permissions na protected API endpoints.'],
      ['Reports','Taarifa','Dashboards na takwimu zinazosaidia kuona mwenendo wa biashara.'],
    ],
    howTitle:'Jinsi inavyofanya kazi', howSub:'Mtiririko rahisi kutoka usajili hadi ufuatiliaji wa marejesho.',
    steps:[['01','Jisajili','Weka taarifa zinazohitajika na tuma ombi lako.'],['02','Kaguliwa','Admin anakagua taarifa na kuidhinisha akaunti.'],['03','Simamia','Tengeneza na fuatilia mikopo kulingana na role yako.'],['04','Fuatilia malipo','Rekodi na kagua marejesho hadi mkopo ukamilike.']],
    securityTitle:'Imejengwa kwa kuzingatia usalama', securitySub:'Mfumo unaweka ruhusa, uthibitishaji na ufuatiliaji katikati ya workflow.',
    ctaTitle:'Tayari kuanza?', ctaText:'Fungua akaunti na uanze kutumia mfumo wa JmkLoanApp.', cta:'Fungua akaunti',
    footer:'© 2026 JmkLoanApp. Loan management platform.',
  },
  en: {
    nav: { features:'Features', how:'How it works', security:'Security', contact:'Contact', login:'Login', register:'Register' },
    hero: { badge:'LOAN MANAGEMENT PLATFORM', title:'Manage loans with clarity, security and control.', text:'JmkLoanApp connects borrowers, lenders and guarantors in one modern platform for loan and payment tracking.', primary:'Get started', secondary:'Sign in' },
    trust: ['JWT authentication','Role-based access','Payment tracking','Responsive design'],
    featuresTitle:'Everything important, in one place',
    featuresSub:'Tools designed for practical day-to-day loan management.',
    features:[
      ['Calculator','Repayment calculator','Estimate interest, fees and repayments before creating a loan.'],
      ['Workflow','Loan workflow','Create, review, approve, disburse and track loan status.'],
      ['Payments','Payments','Record payments and follow history, balance and confirmation.'],
      ['Guarantor','Guarantors','Manage guarantor requests and decisions securely.'],
      ['Security','Security','JWT, role-based permissions and protected API endpoints.'],
      ['Reports','Insights','Dashboards and statistics that help you understand activity.'],
    ],
    howTitle:'How it works', howSub:'A simple flow from registration to repayment tracking.',
    steps:[['01','Register','Provide the required information and submit your application.'],['02','Review','An administrator reviews and approves the account.'],['03','Manage','Create and track loans according to your role.'],['04','Track payments','Record and review repayments until the loan is complete.']],
    securityTitle:'Built with security in mind', securitySub:'Permissions, authentication and protected workflows are core parts of the platform.',
    ctaTitle:'Ready to get started?', ctaText:'Create an account and start using JmkLoanApp.', cta:'Create account',
    footer:'© 2026 JmkLoanApp. Loan management platform.',
  }
} as const;

export function getInitialLanguage(): Lang {
  if (typeof window === 'undefined') return 'sw';
  const saved = localStorage.getItem('jmk-language');
  if (saved === 'sw' || saved === 'en') return saved;
  return (navigator.language || '').toLowerCase().startsWith('sw') ? 'sw' : 'en';
}
