# Parameter Validator

This repository contains a proof of concept.
It is mainly to work with reflection and also try out the idea.

To see the caveats and learnings, scroll to the bottom below example.

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

An optional parameter `language` can be provided.
It allows `english`, `german` and `irish`.

```json
{
  "name":"EncyclopediaArticleRequestValidation",
  "parameters":{
    "articleId":{
      "required":true,
      "validators":[
        {
          "name":"regEx",
          "parameters":{
            "pattern":"[a-z]+"
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
    "language":{
      "required":false,
      "validators":[
        {
          "name":"setOf",
          "parameters":{
            "set":[
              "english", "german", "irish"
            ]
          }
        }
      ]
    }
  }
}
```

## Learnings, Caveats

As this is a proof of concept, I find it reasonable to share learnings as well.
One main learning is that by default Jackson does not enforce `null` for primitive data types.
To do so, one needs to configure the mapper with `DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES`.

The main caveat lies in using reflection to resolve the validator classes at runtime.
Whilst that primarily was "for fun" reasons, it has more downsides than benefits (if there is any).

### Validators need no-arg constructor

That is due to the nature of how the resolver creates their instances, to then call validate.
Not providing argument constructors drastically limits the ability of the validators.
Suppose the `setOf` validator wants to read from a cache or database.
The clients could not be provided to the class (except using more reflection).

### Validate function needs to match format

Another downside is that the `validate` function needs to be unique and accept exactly two parameters.
Moreover, these parameters need to be in the order that the invoker expects them.
That is, the first parameter **must** be the value that it expects.
The second parameter **must** be what the class expects to retrieve as parameters from the template.

### Tackle the previous issues

Both of these issues could be tackled by avoiding runtime resolution using reflection.
An enhancement could be introducing the interface

```kotlin
interface Validator<I, P> {
    fun validate(value: I, parameters: P)
}
```

that must be implemented by all validators.
They could then be registered to a resolver.
The only reflection needed would then be to find out I and P at runtime to cast the provided values.
Side note to the previous sentence:
I have not checked whether these types are only available at compile time.
