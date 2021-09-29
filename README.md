#PrettyGsonWriter

PrettyGsonWriter is a custom GsonWriter that just more prettyfied.
How can Gson be more improved in PrettyGson?

in 2 Aspects actually fairly easy.

- 1: Arrays go vertically to hell and make reading a Json File annoying/unbearable without a Treefication.
- 2: Some Objects are just things you could compress to reduce size and still have full readablilty.

Now this Pretty Gson Writer has its limits. Its not Perfect but it improves the Json Experience massivly.

Here is Ugly Example
<details>
<summary>Ugly Json</summary>
<p>

```json
{
	"info": {
		"name": "Example",
		"id": "20847246-ada6-4dbd-9fd6-a75892c59154",
		"vertecies": 24,
		"indecies": 36
	},
	"format": {
		"dataOrder": [
			{
				"type": "position",
				"length": 3,
				"glType": "float"
			},
			{
				"type": "color",
				"length": 4,
				"glType": "byte"
			},
			{
				"type": "normal",
				"length": 3,
				"glType": "float"
			},
			{
				"type": "uvs",
				"length": 2,
				"glType": "float",
				"optional": true
			}
		],
		"useIndecies": true
	},
	"data": {
		"position": [
			-0.5,
			1.0,
			0.5,
			0.0
		],
		"color": [
			-1
		],
		"normal": [
			0.0,
			1.0,
			-1.0
		],
		"uv": [
			0.875,
			0.5,
			0.625,
			0.75,
			0.375,
			1.0,
			0.0,
			0.25,
			0.125
		],
		"vertexes": [
			0,
			1,
			0,
			0,
			0,
			0,
			0,
			0,
			1,
			0,
			0,
			1,
			2,
			1,
			2,
			0,
			0,
			0,
			0,
			0,
			1,
			0,
			2,
			3,
			2,
			1,
			0,
			0,
			0,
			0,
			0,
			0,
			1,
			0,
			2,
			1,
			2,
			1,
			2,
			0,
			0,
			0,
			0,
			0,
			0,
			1,
			2,
			3,
			0,
			3,
			2,
			0,
			0,
			0,
			0,
			0,
			0,
			1,
			4,
			5,
			2,
			3,
			2,
			0,
			0,
			0,
			0,
			0,
			0,
			1,
			4,
			3,
			0,
			1,
			2,
			0,
			0,
			0,
			0,
			2,
			0,
			0,
			2,
			6,
			0,
			3,
			0,
			0,
			0,
			0,
			0,
			2,
			0,
			0,
			4,
			7,
			0,
			3,
			2,
			0,
			0,
			0,
			0,
			2,
			0,
			0,
			4,
			6,
			2,
			3,
			0,
			0,
			0,
			0,
			0,
			0,
			2,
			0,
			4,
			1,
			0,
			3,
			2,
			0,
			0,
			0,
			0,
			0,
			2,
			0,
			8,
			3,
			0,
			3,
			0,
			0,
			0,
			0,
			0,
			0,
			2,
			0,
			8,
			1,
			2,
			1,
			0,
			0,
			0,
			0,
			0,
			1,
			0,
			0,
			2,
			1,
			2,
			3,
			2,
			0,
			0,
			0,
			0,
			1,
			0,
			0,
			4,
			3,
			2,
			3,
			0,
			0,
			0,
			0,
			0,
			1,
			0,
			0,
			4,
			1,
			0,
			1,
			0,
			0,
			0,
			0,
			0,
			0,
			0,
			2,
			2,
			7,
			2,
			3,
			0,
			0,
			0,
			0,
			0,
			0,
			0,
			2,
			4,
			1,
			0,
			3,
			0,
			0,
			0,
			0,
			0,
			0,
			0,
			2,
			4,
			7,
			0,
			1,
			2,
			0,
			0,
			0,
			0,
			0,
			1,
			0,
			0,
			3,
			0,
			1,
			2,
			0,
			0,
			0,
			0,
			0,
			0,
			1,
			2,
			5,
			0,
			1,
			0,
			0,
			0,
			0,
			0,
			2,
			0,
			0,
			2,
			7,
			2,
			3,
			2,
			0,
			0,
			0,
			0,
			0,
			2,
			0,
			4,
			3,
			2,
			1,
			2,
			0,
			0,
			0,
			0,
			1,
			0,
			0,
			2,
			3,
			2,
			1,
			0,
			0,
			0,
			0,
			0,
			0,
			0,
			2,
			2,
			1
		],
		"indecies": [
			0,
			1,
			2,
			3,
			4,
			5,
			6,
			7,
			8,
			9,
			10,
			11,
			12,
			13,
			14,
			15,
			16,
			17,
			0,
			18,
			1,
			3,
			19,
			4,
			6,
			20,
			7,
			9,
			21,
			10,
			12,
			22,
			13,
			15,
			23,
			16
		]
	},
	"children": []
}
```

</p>
</details>

And here is a Pretty Example

<details>
<summary>Pretty Json</summary>
<p>

```json
{
	"info": {
		"name": "Example",
		"id": "20847246-ada6-4dbd-9fd6-a75892c59154",
		"vertecies": 24,
		"indecies": 36
	},
	"format": {
		"dataOrder": [
			{"type": "position", "length": 3, "glType": "float"},
			{"type": "color", "length": 4, "glType": "byte"},
			{"type": "normal", "length": 3, "glType": "float"},
			{"type": "uvs", "length": 2, "glType": "float", "optional": true}
		],
		"useIndecies": true
	},
	"data": {
		"position": [-0.5, 1.0, 0.5, 0.0],
		"color": [-1],
		"normal": [0.0, 1.0, -1.0],
		"uv": [0.875, 0.5, 0.625, 0.75, 0.375, 1.0, 0.0, 0.25, 0.125],
		"vertexes": [0, 1, 0, 0, 0, 0, 0, 0, 1, 0, 0, 1, 2, 1, 2, 0, 0, 0, 0, 0, 1, 0, 2, 3, 2, 1, 0, 0, 0, 0, 0, 0, 1, 0, 2, 1, 2, 1, 2, 0, 0, 0, 0, 0, 0, 1, 2, 3, 0, 3, 2, 0, 0, 0, 0, 0, 0, 1, 4, 5, 2, 3, 2, 0, 0, 0, 0, 0, 0, 1, 4, 3, 0, 1, 2, 0, 0, 0, 0, 2, 0, 0, 2, 6, 0, 3, 0, 0, 0, 0, 0, 2, 0, 0, 4, 7, 0, 3, 2, 0, 0, 0, 0, 2, 0, 0, 4, 6, 2, 3, 0, 0, 0, 0, 0, 0, 2, 0, 4, 1, 0, 3, 2, 0, 0, 0, 0, 0, 2, 0, 8, 3, 0, 3, 0, 0, 0, 0, 0, 0, 2, 0, 8, 1, 2, 1, 0, 0, 0, 0, 0, 1, 0, 0, 2, 1, 2, 3, 2, 0, 0, 0, 0, 1, 0, 0, 4, 3, 2, 3, 0, 0, 0, 0, 0, 1, 0, 0, 4, 1, 0, 1, 0, 0, 0, 0, 0, 0, 0, 2, 2, 7, 2, 3, 0, 0, 0, 0, 0, 0, 0, 2, 4, 1, 0, 3, 0, 0, 0, 0, 0, 0, 0, 2, 4, 7, 0, 1, 2, 0, 0, 0, 0, 0, 1, 0, 0, 3, 0, 1, 2, 0, 0, 0, 0, 0, 0, 1, 2, 5, 0, 1, 0, 0, 0, 0, 0, 2, 0, 0, 2, 7, 2, 3, 2, 0, 0, 0, 0, 0, 2, 0, 4, 3, 2, 1, 2, 0, 0, 0, 0, 1, 0, 0, 2, 3, 2, 1, 0, 0, 0, 0, 0, 0, 0, 2, 2, 1],
		"indecies": [0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 0, 18, 1, 3, 19, 4, 6, 20, 7, 9, 21, 10, 12, 22, 13, 15, 23, 16]
	},
	"children": []
},
```

</p>
</details>

Now this is a lot better to read.
With this Pretty JsonWriter you are forced to use these "Compressed Value Arrays". This is a rule that can not be turned off.
But the Objects that are single lined are fully optional and you just define a "key" that should follow that rule.
In this case the array "dataOrder" was defined to be compressed.

Also Compressed Elements get a "space" after each ","

How this was achieved? It just applies a simple ruleset on to the JsonWriter and either blocks the writing of certain elements or exchanges them with something new.

here is some example code how to write with this Pretty Json Writer:

```java
public void write(JsonObject obj)
{
	try(JsonWriter json = new PrettyGsonWriter(writer, "\t").addSinlgeLines("dataOrder"))
	{
		Streams.write(obj, json);
	}
	catch(Exception e) {
		e.printStackTrace();
	}
}
```

Indents are optionally set in the Constructor or the setTabs function because Json made these functions not overrideable.
Add single line in this case defines Arrays that should compress its objects. (Compression is also not recursive)

Anyone who wants to use this it is free to use under the Apache-2 license.