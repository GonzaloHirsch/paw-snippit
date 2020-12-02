import SnippetFeedHOC from "./snippet_feed_hoc";
import SnippetUpvotedFeed from "../snippets/snippet_upvoted_feed";
import store from "../../store";

const userId = store.getState().auth.info.uid;

const Upvoted = SnippetFeedHOC(
  SnippetUpvotedFeed,
  (SnippetFeedClient, page) => SnippetFeedClient.getUpvotedSnippetFeed(page, userId),
  (SnippetFeedClient, page, search) =>
    SnippetFeedClient.searchUpvotedSnippetFeed(page, userId, search)
);

export default Upvoted;
