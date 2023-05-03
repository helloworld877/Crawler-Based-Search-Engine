import React from "react";

function SearchResult(props) {
    var results = [
        {
            URL: "https://example.com",
            TITLE: "Dummy Title",
            KEYWORDS:
                "Lorem ipsum dolor sit amet consectetur adipisicing elit. Maxime mollitia, molestiae quas vel sint commodi repudiandae consequuntur voluptatum laborumnumquam blanditiis harum quisquam eius sed odit fugiat iusto fuga praesentiumoptio, eaque rerum! Provident similique accusantium nemo autem. Veritatisobcaecati tenetur iure eius earum ut molestias architecto voluptate aliquamnihil, eveniet aliquid culpa officia aut! Impedit sit sunt quaerat, odit,",
        },
    ];

    return (
        <div>
            <div>
                <h2>{props.q}</h2>
                <a href={results[0].URL}>{results[0].URL}</a>
                <h4>{results[0].TITLE}</h4>
                <p>{results[0].KEYWORDS}</p>
            </div>
            <div>
                <a href={results[0].URL}>{results[0].URL}</a>
                <h4>{results[0].TITLE}</h4>
                <p>{results[0].KEYWORDS}</p>
            </div>

            <div>
                <a href={results[0].URL}>{results[0].URL}</a>
                <h4>{results[0].TITLE}</h4>
                <p>{results[0].KEYWORDS}</p>
            </div>

            <div>
                <a href={results[0].URL}>{results[0].URL}</a>
                <h4>{results[0].TITLE}</h4>
                <p>{results[0].KEYWORDS}</p>
            </div>
        </div>
    );
}
export default SearchResult;
