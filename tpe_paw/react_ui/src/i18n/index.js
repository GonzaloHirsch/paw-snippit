import i18next from 'i18next';
import en from './en';
import es from './es';
import LngDetector from 'i18next-browser-languagedetector';

i18next
  .use(LngDetector)
  .init({
    interpolation: {
      escapeValue: false,
    },
    lng: navigator.language || navigator.userLanguage,
    fallbackLng: "en",
    resources: {
      es: {translation: es},
      en: {translation: en},
    },
  });

export default i18next;