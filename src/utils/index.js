export const returnOtions = (options) => {
    return options?.map(option => {
        return {
            label: option,
            value: option
        }
    })
}

export const convertArray = (string = '', commant) => {
    return string?.split(commant)
}

export const convertString = (array = [], commant) => {
    if (array === '') array = []

    return array?.join(commant)
}