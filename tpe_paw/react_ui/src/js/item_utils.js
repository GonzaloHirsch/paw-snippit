export function getItemPositionInArray(items, id){
    for (let i in items){
      if (items[i].id === id){
        return i;
      }
    }
    return -1;
  }