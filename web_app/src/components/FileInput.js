import {useState} from 'react';

export default function FileInput({onChange}) {
    // const [fileName, setFileName] = useState(null);

    const handleFileChange = event => {
        if (event.target.files) {
            // setFileName(event.target.files[0].name);
            onChange(event.target.files[0]);
        }
    };

    return (
        <div>
            <input type="file" onChange={handleFileChange} />
            {/*<div>{fileName}</div>*/}
        </div>
    );
}