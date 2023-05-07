import React from "react";

function SearchResult(props) {
    return (
        <div>
            <div>
                <h4>{props.res.TITLE}</h4>
                <a href={props.res.URL}>{props.res.URL}</a>
                <hr></hr>
                {/* <p>{results[0].KEYWORDS}</p> */}
            </div>
        </div>
    );
}
export default SearchResult;
