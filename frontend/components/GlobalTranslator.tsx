'use client';

import { useEffect } from 'react';

const SW_TO_EN: Record<string,string> = {
 'Ingia':'Login','Jisajili':'Register','Toka':'Logout','Rudi':'Back','Watumiaji':'Users','Matangazo':'Announcements','Ripoti':'Reports','Mipangilio':'Settings',
 'Dashboard ya Admin':'Admin Dashboard','Bidhaa za Mikopo':'Loan Products','Vipengele Vyote':'All Features','Kagua watumiaji':'Review users',
 'Mikopo Yangu':'My Loans','Fungua':'Open','Funga':'Close','Tafuta':'Search','Tafuta...':'Search...','Inapakia mikopo...':'Loading loans...',
 'Hakuna watumiaji.':'No users.','Hakuna malipo bado.':'No payments yet.','Malipo':'Payments','Mikopo':'Loans','Mkopo':'Loan',
 'Unda Mkopo':'Create Loan','Idhinisha':'Approve','Kataa':'Reject','Thibitisha':'Confirm','Tuma Malipo':'Submit Payment',
 'Fanya Malipo':'Make Payment','Ratiba ya Marejesho':'Repayment Schedule','Historia ya Malipo':'Payment History','Muhtasari':'Summary',
 'Jina':'Name','Simu':'Phone','Barua pepe':'Email','Password':'Password','Hali ya sasa':'Current status','Hali ya akaunti':'Account status',
 'Madhumuni ya mkopo':'Loan purpose','Kiasi cha malipo':'Payment amount','Jumla ya kurejesha':'Total repayment','Muda':'Duration',
 'Kipato cha mwezi':'Monthly income','Anwani':'Address','Mji':'City','Nchi':'Country','Utaifa':'Nationality',
 'Jinsia':'Gender','Hali ya ndoa':'Marital status','Kazi':'Occupation','Mwajiri':'Employer','Kitambulisho':'ID type',
 'Namba ya kitambulisho':'ID number','Tarehe ya kuzaliwa':'Date of birth','Ndugu wa karibu':'Next of kin','Uhusiano':'Relationship',
 'Wanasubiri':'Pending','Wamekubaliwa':'Approved','Wamekataliwa':'Rejected','Wote':'All',
 'Imeshindikana kupakia mikopo.':'Failed to load loans.','Imeshindikana kupakia orodha':'Failed to load list',
 'Hakuna mkopo unaolingana na utafutaji wako.':'No loan matches your search.','Imetolewa':'Disbursed','Imelipwa':'Paid','Imechelewa':'Overdue',
 'Inasubiri':'Pending','Imeidhinishwa':'Approved','Imekataliwa':'Rejected','Amekubaliwa':'Approved','Simamisha':'Suspend','Washa':'Activate',
 'Lugha':'Language','Lugha ya sasa':'Current language','Muonekano':'Appearance','Taarifa za mfumo':'System information',
 'Hali ya akaunti imebadilishwa.':'Account status updated.','Role imebadilishwa.':'Role updated.','Vitendo vya haraka':'Quick actions',
 'Matukio vya haraka':'Quick actions','Jinsi inavyofanya kazi':'How it works','Usalama':'Security','Mawasiliano':'Contact',
 'Weka Password':'Set Password','Reset Password':'Reset Password','Anza sasa':'Get started','Fungua akaunti':'Create account',
 'Tayari kuanza?':'Ready to get started?','Jina kamili linahitajika':'Full name is required','Mji unahitajika':'City is required',
 'Lazima uwe na umri wa miaka 18 au zaidi':'You must be at least 18 years old','Barua pepe au password si sahihi':'Invalid email or password',
 'Akaunti yako haijakubaliwa bado':'Your account has not been approved yet'
};

const EN_TO_SW: Record<string,string> = Object.fromEntries(Object.entries(SW_TO_EN).map(([a,b])=>[b,a]));

function apply(root: Node, dict: Record<string,string>) {
 const walker=document.createTreeWalker(root,NodeFilter.SHOW_TEXT);
 const nodes: Text[]=[]; let n: Node|null;
 while((n=walker.nextNode())) nodes.push(n as Text);
 for(const node of nodes){
   const parent=(node.parentElement?.tagName||'').toLowerCase();
   if(['script','style','code','pre','textarea'].includes(parent)) continue;
   const raw=node.nodeValue||''; const trimmed=raw.trim();
   if(!trimmed) continue;
   if(dict[trimmed]) node.nodeValue=raw.replace(trimmed,dict[trimmed]);
 }
 root.querySelectorAll('input[placeholder],textarea[placeholder],button[title],*[aria-label]').forEach((el)=>{
   for(const attr of ['placeholder','title','aria-label']){
     const v=el.getAttribute(attr); if(v&&dict[v]) el.setAttribute(attr,dict[v]);
   }
 });
}

export default function GlobalTranslator(){
 useEffect(()=>{
   let current=localStorage.getItem('jmk-language')==='en'?'en':'sw';
   const run=()=>apply(document.body,current==='en'?SW_TO_EN:EN_TO_SW);
   const observer=new MutationObserver(()=>requestAnimationFrame(run));
   observer.observe(document.body,{subtree:true,childList:true,characterData:true});
   const handler=(e:Event)=>{current=(e as CustomEvent<'sw'|'en'>).detail;run();};
   window.addEventListener('jmk-language-change',handler);
   run();
   return()=>{observer.disconnect();window.removeEventListener('jmk-language-change',handler);};
 },[]);
 return null;
}
