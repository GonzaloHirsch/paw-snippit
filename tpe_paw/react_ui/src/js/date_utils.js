export function getDateFromString(stringDate) {
  if (stringDate !== null && stringDate !== undefined && stringDate !== "") {
    var parts = stringDate.split("/");
    // Please pay attention to the month (parts[1]); JavaScript counts months from 0:
    // January - 0, February - 1, etc.
    var date = new Date(parts[2], parts[1] - 1, parts[0]);
    return date;
  }
  return null;
}
