import React from 'react';
import logo from './logo.svg';
import './App.css';
import NavBar from './components/navigation/navbar';
import SnippetCard from './components/snippets/snippet_card'

function App() {
  return (
    <React.Fragment>
      <NavBar/>
      <SnippetCard/>
    </React.Fragment>
  );
}

export default App;
