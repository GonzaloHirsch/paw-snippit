import React, { Component } from "react";

class SnippetCard extends Component {
  state = {};
  render() {
    const { snippet } = this.props;

    return (
      <div
        className="card mb-3 bg-light shadow bg-white rounded"
        style={{ maxWidth: "40rem" }}
      >
        <div className="card-header px-4" style={{ fontSize: "20px" }}>
          <div className="row align-items-center">
            <img src="/userIcon.jpg" alt="User Icon" />
            <div className="col ml-2">
              <div className="row text-primary" style={{ fontSize: "18px" }}>
                ghirsch
              </div>
              <div className="row text-muted" style={{ fontSize: "12px" }}>
                10/10/2020
              </div>
            </div>
            <div
              className="badge badge-pill badge-primary"
              style={{ fontSize: "20px" }}
            >
              Java
            </div>
          </div>
        </div>
        <div className="card-body p-3">
          <h4 className="card-title">MergeSort</h4>
          <h6 className="card-subtitle mb-2 text-muted">
            This is an algorithm to sort by merging
          </h6>
          <div
            className="card-text"
            style={{
              maxHeight: 200,
              overflow: "hidden",
              position: "relative",
              textAlign: "justify",
              whiteSpace: "pre-wrap",
            }}
          >
            <pre
              style={{
                maxHeight: 200,
                overflow: "hidden",
                position: "relative",
                textAlign: "justify",
                whiteSpace: "pre-wrap",
              }}
            >
              <code>
                # First we have a List that contains duplicates: mylist = ["a",
                "b", "a", "c", "c"] # Create a dictionary, using the List items
                as keys. This will automatically remove any # duplicates because
                dictionaries cannot have duplicate keys. # Then, convert the
                dictionary back into a list: mylist =
                list(dict.fromkeys(mylist)) # Now we have a List without any
                duplicates, and it has the same order as the original List. #
                Print the List to demonstrate the result print(mylist)
              </code>
            </pre>
            <p className="card-snippet-fade-out card-snippet-fade-out-code hidden"></p>
          </div>
        </div>
      </div>
    );
  }
}

export default SnippetCard;
