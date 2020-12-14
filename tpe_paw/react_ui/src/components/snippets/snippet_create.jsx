import SnippetCreateForm from "../forms/snippet_create_form";
import store from "../../store";
import i18n from "../../i18n";

const SnippetCreate = () => {
  const state = store.getState();
  if (state.auth.token === null || state.auth.token === undefined) {
    // ERROR should not be here if you are not logged in
  }

  return (
    <div className="parent-width flex-center flex-col">
      <h1 className="fw-100 my-2">{i18n.t("snippetCreate.header")}</h1>
      <div
        className="flex-center shadow rounded-border p-5"
        style={{ backgroundColor: "white", maxWidth: "680px" }}
      >
        <SnippetCreateForm token={state.auth.token} />
      </div>
    </div>
  );
};

export default SnippetCreate;
