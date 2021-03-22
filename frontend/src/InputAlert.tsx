export default function InputAlert({fromInput, toInput}: {fromInput: string ,toInput: string}) {

    const invalidValue = getInvalidValue(fromInput, toInput)

    if (invalidValue !== "") {
        return (
            <div className="Alert">
                The date {invalidValue} is not valid. It should be in the form: yyyy-mm-dd, e.g., 2020-01-01.
            </div>
        )
    } else {
        return <div></div>
    }


    function getInvalidValue(fromInput: string, toInput: string): string {
        const regex : RegExp = new RegExp('\\d{4}-\\d{2}-\\d{2}')
        
        if (!regex.test(fromInput)) {
            return fromInput
        } else if (!regex.test(toInput)) {
            return toInput
        } else {
            return ""
         }
    }
}