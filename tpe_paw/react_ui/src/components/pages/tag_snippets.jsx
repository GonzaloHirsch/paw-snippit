import SnippetFeedHOC from "./snippet_feed_hoc";
import { getNavSearchFromUrl } from "../../js/search_from_url";
import SnippetsForTag from "../snippets/snippets_for_tag";

const TagSnippets = (props) => {
  const SnippetsForTagWrapper = SnippetFeedHOC(
    SnippetsForTag,
    (SnippetFeedClient, page) =>
      SnippetFeedClient.getSnippetsForTag(props.match.params.id, page),
    (SnippetFeedClient, page, search) =>
      SnippetFeedClient.searchSnippetsForTag(
        props.match.params.id,
        page,
        search
      ),
    (url) => getNavSearchFromUrl(url),
    false
  );
  return <SnippetsForTagWrapper />;
};

export default TagSnippets;
