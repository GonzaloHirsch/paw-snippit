export function getDateFromString(stringDate) {
  if (stringDate !== null && stringDate !== undefined && stringDate !== "") {
    if (!/^\d{4}\-\d{1,2}\-\d{1,2}$/.test(stringDate)) return null;
    const parts = stringDate.split("-");
    // Please pay attention to the month (parts[1]); JavaScript counts months from 0:
    // January - 0, February - 1, etc.
    const date = new Date(parts[0], parts[1] - 1, parts[2]);
    return date;
  }
  return null;
}

// EJEMPLO -> 2020-06-25T08:30:00Z
export function getDateFromAPIString(stringDate) {
  if (stringDate !== null && stringDate !== undefined && stringDate !== "") {
    const localDate = new Date(stringDate);
    const options = { year: "numeric", month: "short", day: "numeric" };
    return localDate.toLocaleDateString(navigator.language, options);
  }
  return null;
}
