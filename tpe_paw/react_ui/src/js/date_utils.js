const LocalDate = require("@js-joda/core").LocalDate;

export function getDateFromString(stringDate) {
  if (stringDate !== null && stringDate !== undefined && stringDate !== "") {
    const parts = stringDate.split("/");
    // Please pay attention to the month (parts[1]); JavaScript counts months from 0:
    // January - 0, February - 1, etc.
    const date = new Date(parts[2], parts[1] - 1, parts[0]);
    return date;
  }
  return null;
}

// EJEMPLO -> 2020-06-25T08:30:00Z
export function getDateFromAPIString(stringDate) {
  if (stringDate !== null && stringDate !== undefined && stringDate !== "") {
    const localDate = new Date(stringDate);
    return localDate.toString().substring(4, 15);
  }
}

// function getLocaleDateString() {
//   const formats = {
//     "el-GR": "d/M/yyyy",
//     "en-029": "MM/dd/yyyy",
//     "en-AU": "d/MM/yyyy",
//     "en-BZ": "dd/MM/yyyy",
//     "en-CA": "dd/MM/yyyy",
//     "en-GB": "dd/MM/yyyy",
//     "en-IE": "dd/MM/yyyy",
//     "en-IN": "dd-MM-yyyy",
//     "en-JM": "dd/MM/yyyy",
//     "en-MY": "d/M/yyyy",
//     "en-NZ": "d/MM/yyyy",
//     "en-PH": "M/d/yyyy",
//     "en-SG": "d/M/yyyy",
//     "en-TT": "dd/MM/yyyy",
//     "en-US": "M/d/yyyy",
//     "en-ZA": "yyyy/MM/dd",
//     "en-ZW": "M/d/yyyy",
//     "es-AR": "dd/MM/yyyy",
//     "es-BO": "dd/MM/yyyy",
//     "es-CL": "dd-MM-yyyy",
//     "es-CO": "dd/MM/yyyy",
//     "es-CR": "dd/MM/yyyy",
//     "es-DO": "dd/MM/yyyy",
//     "es-EC": "dd/MM/yyyy",
//     "es-ES": "dd/MM/yyyy",
//     "es-GT": "dd/MM/yyyy",
//     "es-HN": "dd/MM/yyyy",
//     "es-MX": "dd/MM/yyyy",
//     "es-NI": "dd/MM/yyyy",
//     "es-PA": "MM/dd/yyyy",
//     "es-PE": "dd/MM/yyyy",
//     "es-PR": "dd/MM/yyyy",
//     "es-PY": "dd/MM/yyyy",
//     "es-SV": "dd/MM/yyyy",
//     "es-US": "M/d/yyyy",
//     "es-UY": "dd/MM/yyyy",
//     "es-VE": "dd/MM/yyyy",
//     "et-EE": "d.MM.yyyy",
//     "eu-ES": "yyyy/MM/dd",
//   };

//   return formats[navigator.language] || "dd/MM/yyyy";
// }
