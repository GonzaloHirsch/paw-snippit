import SnippetFeedHOC from "./snippet_feed_hoc";
import { getNavSearchFromUrl } from "../../js/search_from_url";
import SnippetsForLanguage from "../snippets/snippets_for_language";

const LanguageSnippets = (props) => {
  const SnippetsForLanguageWrapper = SnippetFeedHOC(
    SnippetsForLanguage,
    (SnippetFeedClient, page) =>
      SnippetFeedClient.getSnippetsForLanguage(props.match.params.id, page),
    (SnippetFeedClient, page, search) =>
      SnippetFeedClient.searchSnippetsForLanguage(
        props.match.params.id,
        page,
        search
      ),
    (url) => getNavSearchFromUrl(url)
  );
  return <SnippetsForLanguageWrapper />;
};

export default LanguageSnippets;
