import React, { Component } from "react";
import LanguagesAndTagsClient from "../../api/implementations/LanguagesAndTagsClient";
import i18n from "../../i18n";
import SnippetFeed from "./snippet_feed";

class SnippetsForLanguage extends Component {
  languagesAndTagsClient;

  constructor(props) {
    super(props);
    this.languagesAndTagsClient = new LanguagesAndTagsClient();

    this.state = {
      language: { id: -1, name: "..." },
    };
  }

  _loadLanguageInfo() {
    this.languagesAndTagsClient
      .getLanguageWithId(this.props.match.params.id)
      .then((res) => {
        this.setState({ language: res.data });
      });
  }

  componentDidMount() {
    this._loadLanguageInfo();
  }

  render() {
    const {
      snippets,
      totalSnippets,
      links,
      currentPage,
      onPageTransition,
      onSnippetFav,
      userIsLogged,
    } = this.props;
    return (
      <div className="flex-center flex-column">
        <div className="flex-center mt-4 mb-2">
          <h1 className="fw-300 no-margin">
            {i18n.t("items.snippetsFor", {
              item: this.state.language.name.toUpperCase(),
            })}
          </h1>
        </div>
        <h5 className="fw-100 mb-3">
          ({i18n.t("snippetWithNumber", { count: totalSnippets })})
        </h5>

        <SnippetFeed
          snippets={snippets}
          totalSnippets={totalSnippets}
          links={links}
          currentPage={currentPage}
          onPageTransition={onPageTransition}
          onSnippetFav={onSnippetFav}
          userIsLogged={userIsLogged}
        />
      </div>
    );
  }
}

export default SnippetsForLanguage;
