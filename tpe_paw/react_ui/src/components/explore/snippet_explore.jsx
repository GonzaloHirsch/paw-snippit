import React, { Component } from "react";
import SnippetFeed from "../snippets/snippet_feed";
import LanguageClient from "../../api/implementations/LanguageClient";
import TagClient from "../../api/implementations/TagClient";
import i18n from "../../i18n";
import ExploreForm from "./explore_form";

// Stateless Functional Component

class SnippetExplore extends Component {
  state = {
    languages: [],
    tags: [],
  };

  languageClient;
  tagClient;

  constructor(props) {
    super(props);
    this.languageClient = new LanguageClient();
    this.tagClient = new TagClient();
  }

  loadLanguages() {
    this.languageClient
      .getLanguageList()
      .then((res) => {
        this.setState({
          languages: res.data,
        });
      })
      .catch((e) => {});
  }

  loadTags() {
    this.tagClient
      .getTagList()
      .then((res) => {
        this.setState({
          tags: res.data,
        });
      })
      .catch((e) => {});
  }

  componentDidMount() {
    this.loadLanguages();
    this.loadTags();
  }

  render() {
    return (
      <React.Fragment>
        <div className="mx-3 mb-3 fw-100">
          <h1 className="fw-300">{i18n.t("nav.explore")}</h1>
          <h5 className="fw-100">
            ({this.props.totalSnippets} {i18n.t("snippets")})
          </h5>
        </div>
        <div className="row">
          <div className="col-3 explore-margin p-0">
            <div className="p-3 rounded-border explore-border">
              <ExploreForm
                {...this.state}
                urlSearch={this.props.currentSearch}
              />
            </div>
          </div>

          <div className="col-8">
            <SnippetFeed
              snippets={this.props.snippets}
              links={this.props.links}
              currentPage={this.props.currentPage}
              onPageTransition={this.props.onPageTransition}
              onSnippetFav={props.onSnippetFav}
              userIsLogged={props.userIsLogged}
            />
          </div>
        </div>
      </React.Fragment>
    );
  }
}

export default SnippetExplore;
