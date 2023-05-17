import React from "react";

function SearchResult(props) {
    return (
        <div>
            <div>
                <h4>{props.res.TITLE}</h4>
                <a href={props.res.URL}>{props.res.URL}</a>
                <p>{props.res.SNIPPET}</p>
                <hr></hr>
            </div>
        </div>
    );
}
export default SearchResult;
