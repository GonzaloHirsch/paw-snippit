export function extractLinkHeaders(headers) {
  if (!("link" in headers)) {
    return {};
  }
  const linkHeader = headers["link"];
  const links = linkHeader.split(",");
  let newLinks = {};
  links.forEach((h) => {
    const parts = h
      .trim()
      .replace(/</g, "")
      .replace(/>/g, "")
      .replace(/"/g, "")
      .replace(/;/g, "")
      .split(" ");
    const ref = parts[1].replace("rel=", "");
    const url = parts[0];
    const page = new URLSearchParams(url.split("?")[1]).get("page");
    newLinks[ref] = { url: url, name: ref, page: page };
  });
  return newLinks;
}

export function extractItemCountHeader(headers) {
  const value = headers["x-all-items"];
  return parseInt(value, 10);
}

export function extractResourceIdFromHeaders(headers) {
  const value = headers["location"];
  const parts = value.split("/");
  return parseInt(parts[parts.length - 1], 10);
}
