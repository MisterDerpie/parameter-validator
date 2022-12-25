# Parameter Validator

This repository contains a proof of concept.
It is mainly to work with reflection and also try out the idea.

## Motivation

Many software applications receive parameters provided by the user as their input.
To validate these, a function in the form of `validate(parameters: Map<String, Any?>)` may be found.
As part of the validate function, common checks are if the parameter

- is present if required
- is of the right datatype
- is within its limits (e.g. a set of allowed values, min/max values)
- matches a RegEx pattern.

One needs to dive into the code to find the specification of the validator.
Instead of providing such validate function, one may only implement the very validators.
The set of parameters and validators could then be put into a template, see below.

## Example

This is an example for a validator of a request for an online encyclopedia.
The mandatory parameter is `articleId`.
An `articleId` may only contain lowercase letters and thus has a RegEx verifier.
Moreover, it's length must be at least 1 and at most 10 characters.

An optional parameter `language` defaults to `english`.
Articles are available in `german` and `irish` too.

```json
{
  "templateName":"EncyclopediaArticleRequestValidation",
  "parameters":[
    {
      "name":"articleId",
      "required":true,
      "validators":[
        {
          "name":"regEx",
          "parameters":{
            "pattern":"[a-z]"
          }
        },
        {
          "name":"minLength",
          "parameters":{
            "length":1
          }
        },
        {
          "name":"maxLength",
          "parameters":{
            "length":10
          }
        }
      ]
    },
    {
      "name":"language",
      "required":false,
      "default":"english",
      "validators":[
        {
          "name":"setOf",
          "parameters":{
            "set":[
              "english, german, irish"
            ]
          }
        }
      ]
    }
  ]
}
```