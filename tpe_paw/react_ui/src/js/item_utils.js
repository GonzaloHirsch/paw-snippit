export function getItemPositionInArray(items, id) {
  for (let i in items) {
    if (items[i].id === id) {
      return i;
    }
  }
  return -1;
}

export function getItemPositionInArrayWithName(items, name) {
  for (let i in items) {
    if (items[i].name === name) {
      return i;
    }
  }
  return -1;
}
